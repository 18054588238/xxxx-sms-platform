package com.personal.search.service.impl;

import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.SearchException;
import com.personal.common.model.StandardReport;
import com.personal.search.service.ElasticsearchService;
import com.personal.search.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

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

    private boolean isExists(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index);
        request.id(id).index(index);
        return restHighLevelClient.exists(request,RequestOptions.DEFAULT);
    }
}
