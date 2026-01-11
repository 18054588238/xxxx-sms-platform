package com.personal.common.utils;

import com.personal.common.enums.ExceptionEnums;
import com.personal.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName SnowFlakeUtil
 * @Author liupanpan
 * @Date 2026/1/11
 * @Description 生成雪花算法
 * 生成规则：
 * 雪花算法，是64bit位的正整数
 * 64的个bit位分别是：
 * 最高为：0，代表是一个正整数
 * 41位：存储毫秒级别的时间戳。最大值为 2^41 - 1
 * 10位：存储机房/机器/操作系统/容器/服务的ID;
 * 12位：存储一个序列，自增的值。最大值为 2^12 - 1
 */
@Component
public class SnowFlakeUtil {
    /**
     * 雪花算法中，41位的时间戳可以表示2^41-1个毫秒，换算成年大约是69.7年。
     * 指定了一个开始时间START_STMP（1668096000000L，即2022-11-11），
     * 这样从2022-11-11开始，加上69.7年，就可以用到2092年左右。这样做是为了延长可用时间。
     */
    private static final long START_STMP = 1668096000000L;

    // 机器id 占用的bit位数
    private final long machineIdBits = 5L;
    // 服务id 占用的bit位数
    private final long serviceIdBits = 5L;
    // 序列 占用的bit位数
    private final long sequenceBits = 12L;

    // 机器id 最大值
    private final long maxMachineId = ~(-1L << machineIdBits);
    // 服务id 最大值
    private final long maxServiceId = ~(-1L << serviceIdBits);
    // 序列 最大值
    private final long maxSequenceBits = ~(-1L << sequenceBits);

    // 机器id - 通过配置文件获取
    @Value("${snowflake.machineId:0}")
    private long machineId;
    // 服务id
    @Value("${snowflake.serviceId:0}")
    private long serviceId;
    // 序列
    private long sequence = 0L;// 初始化

    @PostConstruct // 该方法在依赖注入完成后被自动调用。
    public void init() {
        // 在Spring容器初始化Bean之后，会调用这个init()方法。检查machineId和serviceId是否超出范围。
        if (machineId > maxMachineId || serviceId > maxServiceId) {
            System.out.println("机器id或服务id超过最大范围值！");
            throw new ApiException(ExceptionEnums.SNOWFLAKE_OUT_OF_RANGE);
        }
    }

    // 获取系统时间的毫秒值
    private long timeGen() {
        return System.currentTimeMillis();
    }

    // 记录最近一次获取雪花算法id的时间
    private volatile long latestTimestamp = -1L; // 保证线程安全

    // 生成雪花算法id
    public synchronized long nextId() {
        long curTime = timeGen();
        // 避免时间回拨造成出现重复的id
        if (curTime < latestTimestamp) {
            // 说明出现了时间回拨
            throw new ApiException(ExceptionEnums.SNOWFLAKE_TIME_BACK);
        }
        // 当前时间和上一次生成id的时间相等
        if (curTime == latestTimestamp) {
            // 同一毫秒值生成id
            sequence = (sequence + 1) & maxSequenceBits; // 12位，保证结果在0到4095之间。
            // 当sequence达到4095后，再加1就会变成0，所以这里用了一个循环等待到下一毫秒。
            if (sequence == 0) {
                // 说明已超出最大值
                // 不在这一毫秒生成最大值了，等下一毫秒
                curTime = timeGen();
                while (curTime <= latestTimestamp) {
                    // 时间还没动
                    curTime = timeGen();
                }
            }
        } else {
            sequence = 0L;
        }
        latestTimestamp = curTime;
        /**
         * (curTime - START_STMP) 是为了得到相对于起始时间戳的增量时间。否则 就是直接使用从1970年开始的毫秒时间戳
         * 目的是让时间戳部分占用固定的位数（41位），并且从这个起始时间开始计时，可以使得生成的ID在更长的时间内不会重复。
         */
        // 拼接 41bit位的时间，5位的机器，5位的服务 ，12位的序列。                          // 举例
        return  ((curTime - START_STMP) << sequenceBits+serviceIdBits+machineIdBits)//10位 1011 1001 1100 0000 0000
                |(machineId << sequenceBits+serviceIdBits) // 2位 01 0000 0000
                | (serviceId << sequenceBits)// 2位 1100 0000
                | sequence;// 6位 11 0011
        // 执行或运算 1011 1001 1100 0000 0000
        //                      01 0000 0000
        //                         1100 0000
        //                           11 0011
        // 结果      1011 1001 1101 1111 0011

    }

    public static void main(String[] args) {
        SnowFlakeUtil util = new SnowFlakeUtil();
        long l = util.nextId();
        System.out.println(l);

    }
}
