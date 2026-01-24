package com.personal.search.service;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName ElasticsearchService
 * @Author liupanpan
 * @Date 2026/1/20
 * @Description
 */
public interface ElasticsearchService {
    // 向es索引中写数据
    void index(String index,String id,String jsonData) throws IOException;

    // 更新es中的数据
    void update(String index, String id, Map<String,Object> map) throws IOException;
}
