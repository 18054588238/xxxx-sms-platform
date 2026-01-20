package com.personal.search.service;

import java.io.IOException;

/**
 * @ClassName ElasticsearchService
 * @Author liupanpan
 * @Date 2026/1/20
 * @Description
 */
public interface ElasticsearchService {
    // 向es索引中写数据
    void index(String index,String id,String jsonData) throws IOException;
}
