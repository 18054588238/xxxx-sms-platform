package com.personal.strategy.filter.impl;

import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.constants.SmsConstant;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.StrategyException;
import com.personal.common.model.StandardSubmit;
import com.personal.strategy.feign.CacheFeignClient;
import com.personal.strategy.filter.ChainFilter;
import com.personal.strategy.utils.DFAUtil;
import com.personal.strategy.utils.HutoolDFAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @ClassName ChainFilterImpl
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description 敏感词校验
 * 方法一： 利用redis的交集操作判断 短信内容是否含有敏感词，使用ik分词器 将短信内容分词后存储到redis并进行交集操作 --- ik分词器太慢了
 * 方法二： 基于DFA算法实现
 * 方法三： 使用第三方工具类hutool
 */
@Slf4j
@Service(value = "dirtyword")
public class DirtyWordFilterImpl implements ChainFilter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-敏感词校验】   校验ing…………");
        String text = submit.getText();

        List<String> dirtyWords = HutoolDFAUtil.getDirtyWords(text);
        if (dirtyWords != null && !dirtyWords.isEmpty()) {
            // 说明有敏感词
            log.info("【策略模块-敏感词校验】   短信内容包含敏感词信息， dirtyWords = {}",dirtyWords);
            // 校验失败，发送消息到rabbitmq
            submit.setReportState(SmsConstant.REPORT_STATE_FAIL);
            submit.setErrorMsg(ExceptionEnums.HAS_DIRTY_WORD.getMessage()+",dirtyWords ="+dirtyWords.toString());

            rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG,submit);
            // 抛出异常
            throw new StrategyException(ExceptionEnums.HAS_DIRTY_WORD);
        }
    }

    /*@Autowired
    private CacheFeignClient cacheFeignClient;

    // 基于redis的交集实现
    @Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-敏感词校验】   校验ing…………");
        // 将短信内容进行ik分词存储到redis中并进行交集操作
        String text = submit.getText();
        Set<String> ikContents = new HashSet<>();
        StringReader reader = new StringReader(text);

        IKSegmenter ikSegmenter = new IKSegmenter(reader,true);
        Lexeme l = null;
        while (true) {
            try {
                if ((l = ikSegmenter.next()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ikContents.add(l.getLexemeText());
        }
        // 调用缓存的交集方法拿到结果,用完即删，key随机取,
        Set<Object> dirtyWords = cacheFeignClient.setAndInnerStr(UUID.randomUUID().toString(),
                CacheConstant.DIRTY_WORD,
                ikContents.toArray(new String[0]));
        if (dirtyWords != null && !dirtyWords.isEmpty()) {
            // 说明有敏感词
            log.info("【策略模块-敏感词校验】   短信内容包含敏感词信息， dirtyWords = {}",dirtyWords);
        }
    }*/

    // 基于DFA 敏感词树实现
    /*@Override
    public void check(StandardSubmit submit) {
        log.info("【策略模块-敏感词校验】   校验ing…………");
        String text = submit.getText();

        Set<String> dirtyWords = DFAUtil.getDirtyWords(text);
        if (dirtyWords != null && !dirtyWords.isEmpty()) {
            // 说明有敏感词
            log.info("【策略模块-敏感词校验】   短信内容包含敏感词信息， dirtyWords = {}",dirtyWords);
        }
    }*/


}
