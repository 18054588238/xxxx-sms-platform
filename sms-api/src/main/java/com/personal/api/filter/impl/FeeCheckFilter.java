package com.personal.api.filter.impl;

import com.personal.common.Constant.ApiConstant;
import com.personal.common.Constant.CacheConstant;
import com.personal.api.feign.CacheFeignClient;
import com.personal.api.filter.ChainFilter;
import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.ApiException;
import com.personal.common.model.StandardSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName TemplateCheckFilter
 * @Author liupanpan
 * @Date 2026/1/8
 * @Description 校验客户剩余的金额是否充足
 * 计费规则：
 *
 * 短信文字个数超过70字，按67字/条计算费用
 * 如果短信文字个数小于等于70字，按照一条计算即可
 * 说明：
 * 短信内容一条最大是 140个字节，只要短信内容出现了一个中文或者是中文的字符，此时当前的短信内容就全部都要基于UCS-2编码，这个编码每个字占用2字节。
 * 因为想从平台发送短信必须要有签名，签名必须用中文的符号【】包起来，
 * 所以平台所有短信内容的长度都按照一个文字占用2字节去计算，也就是70个字。
 */
@Slf4j
@Service(value = "fee")
public class FeeCheckFilter implements ChainFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;
    /**
     * 只要短信内容的文字长度小于等于70个字，按照一条计算
     */
    private final int MAX_LENGTH = 70;

    /**
     * 如果短信内容的文字长度超过70，67字/条计算
     */
    private final int LOOP_LENGTH = 67;

    private final String BALANCE = "balance";

    @Override
    public void check(StandardSubmit submit) {
        // 用户余额
        Long balance = ((Integer) cacheFeignClient.getFieldValue(CacheConstant.CLIENT_BALANCE + submit.getClientId(),BALANCE)).longValue();

        Long singleFee = ApiConstant.SINGLE_FEE;
        Long totalFee;
        int textLen = submit.getText().length();
        if (textLen <= MAX_LENGTH) {
            totalFee = singleFee;
        } else { // 大于一条
            int stripNum = textLen / LOOP_LENGTH;
            int mod = textLen % LOOP_LENGTH;
            totalFee = mod == 0 ? singleFee * stripNum : (singleFee) * (stripNum+1);
        }
        if (totalFee > balance) {
            log.info("[接口模块-校验 fee 校验失败]");
            throw new ApiException(ExceptionEnums.BALANCE_NOT_ENOUGH);
        }

        submit.setFee(totalFee);
        log.info("[接口模块-校验 fee 校验成功]");
    }
}
