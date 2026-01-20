package com.personal.search.service.impl;

import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.SearchException;
import com.personal.search.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
}
