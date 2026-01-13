package com.personal.common.utils;

import com.personal.common.enums.MobileOperatorEnum;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OperatorUtil
 * @Author liupanpan
 * @Date 2026/1/13
 * @Description
 */
public class OperatorUtil {

    static Map<String,Integer> operatorMap = new HashMap<>();


    static {
        MobileOperatorEnum[] operatorEnums = MobileOperatorEnum.values();
        for (MobileOperatorEnum operatorEnum : operatorEnums) {
            operatorMap.put(operatorEnum.getOperateName(),operatorEnum.getOperateId());
        }
    }

    public static Integer getOperatorId(String operateName) {
        return operatorMap.get(operateName);
    }
}
