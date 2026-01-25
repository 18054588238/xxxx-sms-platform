package com.personal.monitor.utils;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @ClassName RabbitMQUtil
 * @Author liupanpan
 * @Date 2026/1/25
 * @Description
 */
@Slf4j
public class RabbitMQUtil {

    public static int safeGetMessageCount(Channel channel, String queueName) {
        try {
            AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queueName);
            return declareOk.getMessageCount();
        } catch (IOException e) {
            return handleQueueException(channel, e, queueName);
        }
    }

    private static int handleQueueException(Channel channel, IOException e, String queueName) {
        if (e.getCause() instanceof ShutdownSignalException) {
            ShutdownSignalException sse = (ShutdownSignalException) e.getCause();

            // 判断是否为队列不存在错误（错误码404）
            if (isQueueNotFound(sse)) {
                log.info("队列 [{}] 不存在", queueName);
                return 0;
            }
            // 其他通道关闭原因
            log.warn("通道关闭: {}", sse.getMessage());
        } else {
            log.error("IO异常: {}", e.getMessage(), e);
        }

        // 重新创建通道的逻辑需要根据实际情况处理，这里只是示例
        // 注意：这里无法直接重新创建通道，因为需要connection对象，所以通常需要外部处理
        throw new RuntimeException("处理队列操作失败", e);
    }

    private static boolean isQueueNotFound(ShutdownSignalException sse) {
        Object reason = sse.getReason();
        // 队列不存在时，reason 是 AMQP.Channel.Close 类型
        if (reason instanceof AMQP.Channel.Close) {
            AMQP.Channel.Close close = (AMQP.Channel.Close) reason;
            return close.getReplyCode() == 404;
        }
        // 也可以检查异常消息中是否包含404或NOT_FOUND
        String message = sse.getMessage();
        if (message != null && (message.contains("404") || message.contains("NOT_FOUND"))) {
            return true;
        }
        return false;
    }
}
