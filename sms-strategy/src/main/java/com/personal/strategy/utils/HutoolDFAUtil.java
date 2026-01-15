package com.personal.strategy.utils;

import cn.hutool.dfa.WordTree;
import com.personal.common.constants.CacheConstant;
import com.personal.strategy.feign.CacheFeignClient;

import java.util.List;
import java.util.Set;

/**
 * @ClassName HutoolDFAUtil
 * @Author liupanpan
 * @Date 2026/1/15
 * @Description
 */
public class HutoolDFAUtil {

    private static WordTree wordTree = new WordTree();
    static {
        // 获取Spring容器中的cacheClient
        // 调用缓存模块获取敏感词，将敏感词生成敏感词树
        CacheFeignClient cacheFeignClient = (CacheFeignClient) SpringUtil.getBeanByClazz(CacheFeignClient.class);
        Set<String> dirtyWords = cacheFeignClient.getSMemberStr(CacheConstant.DIRTY_WORD);
        wordTree.addWords(dirtyWords);
    }

    public static List<String> getDirtyWords(String text) {
        return wordTree.matchAll(text);
    }
}
