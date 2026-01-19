package com.personal.test.entity;

import lombok.Data;

/**
 * @ClassName ClientChannel
 * @Author liupanpan
 * @Date 2026/1/18
 * @Description
 */
@Data
public class ClientChannel {
    private Long clientId;
    private Long channelId;
    private Integer clientChannelWeight;
    private String clientChannelNumber;
    private boolean isAvailable;
    private boolean isDelete;
}
