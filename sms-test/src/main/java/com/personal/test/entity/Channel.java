package com.personal.test.entity;

import lombok.Data;

/**
 * @ClassName ClientChannel
 * @Author liupanpan
 * @Date 2026/1/18
 * @Description
 */
@Data
public class Channel {
    private Long id;
    private String channelName;
    private Integer channelType;
    private String channelArea;
    private String channelAreaCode;
    private Long channelPrice;
    private Integer channelProtocal;
    private String channelIp;
    private Integer channelPort	;
    private String channelUsername;
    private String channelPassword;
    private String channelNumber;
    private boolean isAvailable;
    private boolean isDelete;
}
