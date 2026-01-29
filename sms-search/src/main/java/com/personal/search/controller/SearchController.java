package com.personal.search.controller;

import com.personal.common.constants.RabbitMQConstants;
import com.personal.search.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SearchController
 * @Author liupanpan
 * @Date 2026/1/29
 * @Description
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
    @Autowired
    ElasticsearchService elasticsearchService;

    @PostMapping("/findSmsByParameters")
    Map<String, Object> findSmsByParameters(@RequestBody Map map) throws IOException {

        return elasticsearchService.findSmsByParameters(map);

    }
}
