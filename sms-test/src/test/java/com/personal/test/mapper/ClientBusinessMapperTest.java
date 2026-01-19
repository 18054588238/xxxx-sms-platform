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
    @Autowired
    private MobileDirtyWordMapper dirtyWordMapper;
    @Autowired
    private MobileBlackMapper mobileBlackMapper;
    @Autowired
    private MobileTransferMapper mobileTransferMapper;
    @Autowired
    private ClientChannelMapper clientChannelMapper;
    @Autowired
    private ChannelMapper channelMapper;


    @Test
    void setChannel() throws JsonProcessingException {
        List<Channel> channels = channelMapper.findAll();
        for (Channel channel : channels) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(channel), Map.class);
            // hSet --- redisTemplate.opsForHash().putAll(name, map)
            /*
            * 适合存储一个对象的多个属性，每个属性作为Hash的一个field
            * */
            cacheFeignClient.setHMap("channel:"+channel.getId(),map);
        }
    }

    @Test
    void setClientChannel() throws JsonProcessingException {
        List<ClientChannel> clientChannels = clientChannelMapper.findAll();
        for (ClientChannel clientChannel : clientChannels) {
            // sAdd --- redisTemplate.opsForSet().add(key, values)
            /*
            * 向Redis的Set数据结构中添加一个或多个成员
            * */
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(clientChannel), Map.class);
            cacheFeignClient.setMap("client_channel:"+clientChannel.getClientId(),map);
        }
    }

    @Test
    void setMobileTransfer() {
        List<MobileTransfer> data = mobileTransferMapper.getMobileTransfer();
        for (MobileTransfer transfer : data) {
            cacheFeignClient.setValue("transfer:"+transfer.getTransferNumber(),transfer.getNowIsp());
        }
    }

    @Test
    void setMobileBlackToRedis() {
        List<MobileBlack> mobileBlackList = mobileBlackMapper.getMobileBlackList();
        for (MobileBlack mobileBlack : mobileBlackList) {
            // 黑名单类型 0-全局黑名单  其他-客户黑名单
            if(mobileBlack.getClientId() == 0){
                // 平台级别的黑名单   black:手机号   作为key
                cacheFeignClient.setValue("black:" + mobileBlack.getBlackNumber(),"1");
            }else{
                // 客户级别的黑名单   black:clientId:手机号
                cacheFeignClient.setValue("black:" + mobileBlack.getClientId() + ":" +mobileBlack.getBlackNumber(),"1");
            }
        }
    }
    @Test
    void setDirtyWordTest() {
        List<String> dirtyWord = dirtyWordMapper.getDirtyWord();
        cacheFeignClient.setSStr("dirtyWord",dirtyWord.toArray(new String[0]));
    }

    @Test
    void clientBusinessMapperTest() throws JsonProcessingException {
        ClientBusiness clientBusiness = clientBusinessMapper.findById(1l);
        ObjectMapper objectMapper = new ObjectMapper();
        // 这里需要注册JavaTimeModule
        objectMapper.registerModule(new JavaTimeModule());
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(clientBusiness), Map.class);
        cacheFeignClient.setHMap("client_business:"+clientBusiness.getApikey(),
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