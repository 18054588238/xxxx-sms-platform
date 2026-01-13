package com.personal.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.personal.test.entity.*;
import com.personal.test.feign.CacheFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootTest
@Slf4j
class ClientMapperTest {
    @Autowired
    private ClientBusinessMapper clientBusinessMapper;
    @Autowired
    private ClientBalanceMapper clientBalanceMapper;
    @Autowired
    private ClientSignMapper clientSignMapper;
    @Autowired
    private ClientTemplateMapper clientTemplateMapper;
    @Autowired
    private CacheFeignClient cacheFeignClient;
    @Autowired
    private MobileAreaMapper mobileAreaMapper;

    @Test
    void clientBusinessMapperTest() throws JsonProcessingException {
        ClientBusiness clientBusiness = clientBusinessMapper.findById(1l);
        ObjectMapper objectMapper = new ObjectMapper();
        // 这里需要注册JavaTimeModule
        objectMapper.registerModule(new JavaTimeModule());
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(clientBusiness), Map.class);
        cacheFeignClient.setMap("client_business:"+clientBusiness.getApikey(),
                map);
        System.out.println(clientBusiness);
    }

    @Test
    void clientBalanceMapperTest() throws JsonProcessingException {
        ClientBalance balance = clientBalanceMapper.findByClientId(1l);
        ObjectMapper objectMapper = new ObjectMapper();
        // 这里需要注册JavaTimeModule
        objectMapper.registerModule(new JavaTimeModule());
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(balance), Map.class);
        cacheFeignClient.setHMap("client_balance:1",map);
        System.out.println(balance);
    }

    @Test
    void clientSignMapperTest() {
        List<ClientSign> clientSigns = clientSignMapper.findByClientId(1l);

        List<Map> value = clientSigns.stream().map(i -> {
            ObjectMapper objectMapper = new ObjectMapper();
            // 这里需要注册JavaTimeModule
            objectMapper.registerModule(new JavaTimeModule());
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(i), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        cacheFeignClient.setMap("client_sign:1",value.toArray(new Map[0]));
        System.out.println(clientSigns);
    }

    @Test
    void clientTemplateMapperTest(){
        List<ClientTemplate> templateList = clientTemplateMapper.findBySignId(15l);

        List<Map> value = templateList.stream().map(i -> {
            ObjectMapper objectMapper = new ObjectMapper();
            // 这里需要注册JavaTimeModule
            objectMapper.registerModule(new JavaTimeModule());
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(i), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        cacheFeignClient.setMap("client_template:15",value.toArray(new Map[0]));
        System.out.println(templateList);
    }

    // 批次大小
    private final Integer BATCH_SIZE = 5000;
    @Test
    void mobileAreaMapperTest() {
        List<MobileArea> mobileAreas = mobileAreaMapper.findAll();
        int totalCount = mobileAreas.size();
        // 总批次
        int totalPages = (totalCount + BATCH_SIZE - 1) / BATCH_SIZE;// 等价与 totalCount/ BATCH_SIZE 向上取整

        for (int page = 0; page < totalPages; page++) {
            int offset = page * BATCH_SIZE;

            Map<String, String> map = mobileAreas.stream()
                    .skip(offset)
                    .limit(BATCH_SIZE)
                    .collect(Collectors.toMap(m->"phase:"+m.getMobileNumber(),
                            i -> i.getMobileArea() + "," + i.getMobileType()));

            cacheFeignClient.pipeline(map);

            log.info("已处理{}/{} 批次",page+1,totalPages);
        }
    }

}