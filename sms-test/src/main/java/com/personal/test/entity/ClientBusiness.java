package com.personal.test.entity;

import lombok.*;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 客户信息表(ClientBusiness)实体类
 *
 * @author makejava
 * @since 2026-01-10 17:32:05
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientBusiness implements Serializable {
    private static final long serialVersionUID = -47708971993321632L;

    private Long id;
/**
     * 公司名
     */
    private String corpname;
/**
     * HTTP接入的密码
     */
    private String apikey;
/**
     * HTTP客户端的IP白名单（多个用,隔开）
     */
    private String ipAddress;
/**
     * 状态报告是否返回：0 不返回 1 返回
     */
    private Integer isCallback;
/**
     * 客户接收状态报告的URL地址
     */
    private String callbackUrl;
/**
     * 联系人
     */
    private String clientLinkname;
/**
     * 密保手机
     */
    private String clientPhone;
/**
     * 策略校验顺序动态实现规则
     */
    private String clientFilters;
/**
     * 创建时间，默认系统时间
     */
    private LocalDateTime created;
/**
     * 创建人id
     */
    private Long createId;
/**
     * 修改时间，默认系统时间
     */
    private LocalDateTime updated;
/**
     * 修改人id
     */
    private Long updateId;
/**
     * 是否删除 0-未删除 ， 1-已删除
     */
    private Integer isDelete;
/**
     * 备用字段1
     */
    private String extend1;
/**
     * 备用字段2
     */
    private String extend2;
/**
     * 备用字段3
     */
    private String extend3;
/**
     * 备用字段4
     */
    private String extend4;

    public Long getId() {
        return id;
    }

    public String getCorpname() {
        return corpname;
    }

    public String getApikey() {
        return apikey;
    }

    public List<String> getIpAddress() {
        if (StringUtils.isNotBlank(ipAddress)) {
            return Arrays.asList(ipAddress.split(","));
        }
        return null;
    }

    public Integer getIsCallback() {
        return isCallback;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getClientLinkname() {
        return clientLinkname;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getClientFilters() {
        return clientFilters;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Long getCreateId() {
        return createId;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public String getExtend1() {
        return extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public String getExtend3() {
        return extend3;
    }

    public String getExtend4() {
        return extend4;
    }
}

