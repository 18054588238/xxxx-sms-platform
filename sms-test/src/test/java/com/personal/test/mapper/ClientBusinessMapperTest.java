package com.personal.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.personal.test.entity.ClientBalance;
import com.personal.test.entity.ClientBusiness;
import com.personal.test.entity.ClientSign;
import com.personal.test.entity.ClientTemplate;
import com.personal.test.feign.CacheFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootTest
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
}