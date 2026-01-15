package com.personal.strategy.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName DFAUtil
 * @Author liupanpan
 * @Date 2026/1/14
 * @Description
 */
public class DFAUtil {
    // 敏感词树
    private static Map dfaMap = new HashMap<>();

    private final String IS_END = "isEnd";
    /**
     * 创建敏感词树
     * dfaMap 的值变化机制：
     * 初始状态：dfaMap 指向一个空的HashMap对象
     * 引用共享：curMap = dfaMap 使两个变量指向同一个对象
     * 间接修改：通过 curMap 的操作 ** 修改这个共享对象的内容**
     * 内容累积：随着敏感词的添加，dfaMap 指向的对象内容逐渐丰富
     * 引用不变：dfaMap 始终指向同一个对象，只是对象内容变了
     * 说明：
     * 1. 在操作 curMap 时，当 curMap 指向 dfaMap 时，我们通过 curMap.put(aChar, wordValueMap) 修改了 curMap 所引用的对象（也就是 dfaMap）的内容。
     */
    public void createDfaMap(Set<String> dirtyWords){
        Map curMap;
        for (String dirtyWord : dirtyWords) {
            curMap = dfaMap; // 在循环每个敏感词时，将当前指针重置到根节点。
            char[] chars = dirtyWord.toCharArray();
            for(int i=0;i< chars.length;i++){
                char aChar = chars[i];
                Map wordValueMap = (Map) curMap.get(aChar);

                if (wordValueMap == null) {
                    // 树中不包含该敏感词
                    wordValueMap = new HashMap<>();
                    curMap.put(aChar,wordValueMap);
                }
                // 该敏感词的value 是map -- wordValueMap
                curMap = wordValueMap; // 在构建树的过程中，将当前指针移动到下一个节点（子节点）
                /*然后执行 curMap = wordValueMap;，此时 curMap 指向刚刚创建的空 Map（即 '中' 对应的子节点）。
                处理第二个字符，假设为 '国'，同样地，会在 curMap 所指向的 Map 中放入键 '国' 和一个新的空 Map。
                注意，此时 curMap 指向的是 dfaMap 中 '中' 对应的那个 Map，所以修改 curMap 就是修改 dfaMap 中 '中' 对应的那个 Map。*/
                if (curMap.containsKey(IS_END)&&curMap.get(IS_END).equals("1")) {
                    continue;
                }
                // 若是最后一个字
                if (i == chars.length - 1) {
                    curMap.put(IS_END,"1");
                } else {
                    curMap.putIfAbsent(IS_END,"0");
                }
            }
        }
    }

    public static void main(String[] args) {
        DFAUtil dfaUtil = new DFAUtil();
        Set<String> dirtyWords = new HashSet<>();
        dirtyWords.add("三胖");
        dirtyWords.add("山炮");
        dirtyWords.add("三胖啊");
        dirtyWords.add("三胖啊啊");
        dfaUtil.createDfaMap(dirtyWords);

        System.out.println(dfaMap);
    }

}
