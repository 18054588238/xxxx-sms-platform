package com.personal.search.service.impl;

import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.SearchException;
import com.personal.common.model.StandardReport;
import com.personal.search.service.ElasticsearchService;
import com.personal.search.utils.SearchUtil;
import com.personal.search.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @ClassName ElasticsearchServiceImpl
 * @Author liupanpan
 * @Date 2026/1/20
 * @Description
 */
@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void index(String index, String id, String jsonData) throws IOException {
        // 给request对象封装索引信息，文档id，以及文档内容
        IndexRequest request = new IndexRequest(index);
        request.id(id).source(jsonData,XContentType.JSON);

        // 向es发送消息
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        if (response.getResult() != DocWriteResponse.Result.CREATED) {
            // 向es添加数据失败
            log.error("【搜索模块】写入数据失败,IndexRequest = {},IndexResponse = {}",request,response);
            throw new SearchException(ExceptionEnums.ERROR_TO_ES_INDEX);
        }
        log.info("【搜索模块】写入数据成功,IndexRequest = {},IndexResponse = {}",request,response);
    }

    @Override
    public void update(String index, String id, Map<String, Object> map) throws IOException {
        // 1. 更新之前检查指定索引下的文档是否存在
        boolean exists = isExists(index, id);
        if (!exists) {// 不存在
            /*
             * 判断是第几次不存在：
             *  如果是第一次，再次放到延迟交换机中，重新投递消息，再次延迟10s
             *  如果不是，则输出error日志，后期再处理
             * */
            // 获取report对象
            StandardReport report = ThreadPoolUtil.getReport();
            ThreadPoolUtil.removeReport();
            Boolean updateStatus = report.getUpdateStatus();
            if (!updateStatus) { // 第一次
                report.setUpdateStatus(true);
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_GATEWAY_NORMAL_EXCHANGE,"",report);
            } else {
                log.error("[搜索模块] 该索引下的文档不存在,index = {},id = {},map = {}",index,id,map);
            }
            return;
        }
        // 存在。执行更新操作
        UpdateRequest request = new UpdateRequest(index, id);
        request.doc(map);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        if (response.getResult() != DocWriteResponse.Result.UPDATED) {
            // 向es添加数据失败
            log.error("【搜索模块】修改数据失败,IndexRequest = {},IndexResponse = {},map = {}",request,response,map);
            throw new SearchException(ExceptionEnums.ERROR_TO_ES_UPDATE);
        }
        log.info("【搜索模块】修改数据成功,IndexRequest = {},IndexResponse = {},map = {}",request,response,map);

    }

    @Override
    public Map<String, Object> findSmsByParameters(Map map) throws IOException {
        // 获取参数
        Object fromObj = map.get("from");
        Object sizeObj = map.get("size");
        Object contentObj = map.get("content");
        Object mobileObj = map.get("mobile");
        Object startTimeObj = map.get("starttime");
        Object stopTimeObj = map.get("stoptime");
        Object clientIDObj = map.get("clientID");

        // clientIDObj可能是List集合，也可能是单个客户id
        List<Long> clientIds = new ArrayList<>();
        if (clientIDObj instanceof List) {
            clientIds = (List<Long>) clientIDObj;
        } else if (StringUtils.isNotBlank(clientIDObj.toString())) {
            clientIds = Collections.singletonList(Long.parseLong(clientIDObj + ""));
        }

        // 封装数据，进行查询
        SearchRequest searchRequest = new SearchRequest(SearchUtil.getIndex()); // 添加index索引

        searchSource(fromObj, sizeObj, mobileObj, contentObj, startTimeObj, stopTimeObj, clientIds, searchRequest);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        // 查询总条数
        long total = 0;
        if (hits.getTotalHits() != null) {
            total = hits.getTotalHits().value;
        }
        SearchHit[] searchHits = hits.getHits();
        List<Map> rows = new ArrayList<>();

        for (SearchHit hit : searchHits) {
            Map<String, Object> row = hit.getSourceAsMap();
            String sendTimeStr = row.get("sendTime")+"";
            row.put("sendTimeStr",sendTimeStr);
            row.put("corpname",row.get("sign"));

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("text");
            if (highlightField != null) {
                Text[] fragments = highlightField.fragments();
                String fragmentString = fragments[0].string();
                // 设置高亮
                row.put("text",fragmentString);
            }

            rows.add(row);
        }
        //5、返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("total",total);
        result.put("rows",rows);

        return result;
    }

    private static void searchSource(Object fromObj, Object sizeObj, Object mobileObj, Object contentObj, Object startTimeObj, Object stopTimeObj, List<Long> clientIds, SearchRequest searchRequest) {
        // 封装搜索参数
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.from(Integer.parseInt(fromObj +""));
        searchSourceBuilder.size(Integer.parseInt(sizeObj +""));

        // 布尔查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (StringUtils.isNotBlank(mobileObj.toString())) {
            boolQueryBuilder.must(QueryBuilders.prefixQuery("mobile", mobileObj +""));
        }
        if (StringUtils.isNotBlank(contentObj.toString())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("text", contentObj));
        }
        if (StringUtils.isNotBlank(startTimeObj.toString())) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("sendTime").gte(startTimeObj +""));
        }
        if (StringUtils.isNotBlank(stopTimeObj.toString())) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("sendTime").lte(stopTimeObj +""));
        }
        if (!CollectionUtils.isEmpty(clientIds)) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("clientId", clientIds));
        }
        // 封装查询条件
        searchSourceBuilder.query(boolQueryBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder();

        HighlightBuilder.Field highLightText = new HighlightBuilder.Field("text");
        highLightText.highlighterType("unified");
        highLightText.preTags("<span style='color:red'>");
        highLightText.postTags("</span>");
        highlightBuilder.field(highLightText);

        searchSourceBuilder.highlighter(highlightBuilder);

        // 执行检索
        searchRequest.source(searchSourceBuilder);
    }

    private boolean isExists(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index);
        request.id(id).index(index);
        return restHighLevelClient.exists(request,RequestOptions.DEFAULT);
    }
}
