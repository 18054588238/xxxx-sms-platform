package com.personal.test.entity;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 短信签名认证表(ClientSign)实体类
 *
 * @author makejava
 * @since 2026-01-10 17:32:08
 */
@Getter
@Data
public class ClientSign implements Serializable {
    private static final long serialVersionUID = 309820471619788989L;
/**
     * 主键
     */
    private Long id;
/**
     * 客户id，对应client_business表
     */
    private Long clientId;
/**
     * 短信签名内容
     */
    private String signInfo;
/**
     * 审核是否通过 0-审核中 1-审核不通过 2-审核已通过
     */
    private Integer signState;
/**
     * 模板类型 0-验证码，通知类，1-营销类
     */
    private Integer signType;
/**
     * 业务网址与场景
     */
    private String businessWeb;
/**
     * 证明文件描述 如：公司营业执照，APP：应用商店APP管理后台截屏，网站名：ICP备案证明，公众号、小程序：微信公众平台管理页面截图，商标：商标注册证书、商标软著权
     */
    private String proveDescr;
/**
     * 证明文件图片链接，多个以,隔开
     */
    private String proveFile;
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

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }

    public void setSignState(Integer signState) {
        this.signState = signState;
    }

    public void setSignType(Integer signType) {
        this.signType = signType;
    }

    public void setBusinessWeb(String businessWeb) {
        this.businessWeb = businessWeb;
    }

    public void setProveDescr(String proveDescr) {
        this.proveDescr = proveDescr;
    }

    public void setProveFile(String proveFile) {
        this.proveFile = proveFile;
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

