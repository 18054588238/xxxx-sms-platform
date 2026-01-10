package com.personal.test.entity;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 短信模板(ClientTemplate)实体类
 *
 * @author makejava
 * @since 2026-01-10 17:32:08
 */
@Getter
@Data
public class ClientTemplate implements Serializable {
    private static final long serialVersionUID = 236224692218900980L;
/**
     * 主键
     */
    private Long id;
/**
     * 签名id，对应client_sign
     */
    private Long signId;
/**
     * 模板内容
     */
    private String templateText;
/**
     * 模板类型 0-验证码类，1-通知类，2-营销类
     */
    private Integer templateType;
/**
     * 审核是否通过 0-审核中 1-审核不通过 2-审核已通过
     */
    private Integer templateState;
/**
     * 使用场景 0-网站 1-APP 2-微信
     */
    private Integer useId;
/**
     * 网站地址（防轰炸，验证码截图）
     */
    private String useWeb;
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


    public void setId(Long id) {
        this.id = id;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public void setTemplateState(Integer templateState) {
        this.templateState = templateState;
    }

    public void setUseId(Integer useId) {
        this.useId = useId;
    }

    public void setUseWeb(String useWeb) {
        this.useWeb = useWeb;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3;
    }

    public void setExtend4(String extend4) {
        this.extend4 = extend4;
    }

}

