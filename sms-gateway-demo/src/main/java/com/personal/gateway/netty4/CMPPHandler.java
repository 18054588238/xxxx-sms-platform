package com.personal.gateway.netty4;


import com.personal.common.constants.CacheConstant;
import com.personal.common.constants.RabbitMQConstants;
import com.personal.common.constants.SmsConstant;
import com.personal.common.enums.Cmpp2ResultEnums;
import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import com.personal.common.utils.MapStructUtil;
import com.personal.gateway.netty4.entity.CmppDeliver;
import com.personal.gateway.netty4.entity.CmppSubmitResp;
import com.personal.gateway.netty4.utils.CmppReportMapUtil;
import com.personal.gateway.netty4.utils.CmppSubmitMapUtil;
import com.personal.gateway.netty4.utils.MsgUtils;
import com.personal.gateway.netty4.utils.SpringUtil;
import com.personal.gateway.runnable.CmppDeliverRunnable;
import com.personal.gateway.runnable.CmppSubmitRunnable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 主要业务 handler,运营商响应信息
 */
public class CMPPHandler extends SimpleChannelInboundHandler {
    private final static Logger log = LoggerFactory.getLogger(CMPPHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext context, Object msg) throws Exception {

        if (msg instanceof CmppSubmitResp){
            CmppSubmitResp resp=(CmppSubmitResp)msg;
            log.info("-------------接收到短信提交应答-------------");
            log.info("----自增id："+resp.getSequenceId());
            log.info("----状态："+ resp.getResult());
            log.info("----第一次响应："+resp.getMsgId());

            // 使用线程池处理任务
            // 获取线程池对象
            ThreadPoolExecutor submitDynamicExecutor = (ThreadPoolExecutor) SpringUtil.getBeanByName("cmppSubmitDynamicExecutor");

            submitDynamicExecutor.execute(new CmppSubmitRunnable(resp));
        }

        if (msg instanceof CmppDeliver){
            CmppDeliver resp=(CmppDeliver)msg;
            // 是否为状态报告 0：非状态报告1：状态报告
            if (resp.getRegistered_Delivery() == 1) {
                // 如果是状态报告的话
                log.info("-------------状态报告---------------");
                log.info("----第二次响应："+resp.getMsg_Id_DELIVRD());
                log.info("----手机号："+resp.getDest_terminal_Id());
                log.info("----状态："+resp.getStat());
                String stat = resp.getStat();
                long msgId = resp.getMsg_Id_DELIVRD();
                // 使用线程池处理任务
                // 获取线程池对象
                ThreadPoolExecutor deliverDynamicExecutor = (ThreadPoolExecutor) SpringUtil.getBeanByName("cpmmDeliverDynamicExecutor");
                deliverDynamicExecutor.execute(new CmppDeliverRunnable(msgId,stat));

            } else {
                //用户回复会打印在这里
                log.info(""+ MsgUtils.bytesToLong(resp.getMsg_Id()));
                log.info(resp.getSrc_terminal_Id());
                log.info(resp.getMsg_Content());
            }

        }
    }

}
