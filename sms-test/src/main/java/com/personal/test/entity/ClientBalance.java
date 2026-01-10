package com.personal.test.entity;



import lombok.Data;

import java.io.Serializable;

import java.util.Date;

/**
* 客户余额表
* @TableName client_balance
*/
@Data
public class ClientBalance implements Serializable {

    /**
    * 主键
    */
    private Long id;
    /**
    * 客户id，对应client_business表
    */
    private Long clientId;
    /**
    * 用户余额（单位：厘）
    */
    private Long balance;
    /**
    * 创建时间，默认系统时间
    */
    private Date created;
    /**
    * 创建人id
    */
    private Long createId;
    /**
    * 修改时间，默认系统时间
    */
    private Date updated;
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

    /**
    * 主键
    */
    private void setId(Long id){
    this.id = id;
    }

    /**
    * 客户id，对应client_business表
    */
    private void setClientId(Long clientId){
    this.clientId = clientId;
    }

    /**
    * 用户余额（单位：厘）
    */
    private void setBalance(Long balance){
    this.balance = balance;
    }

    /**
    * 创建时间，默认系统时间
    */
    private void setCreated(Date created){
    this.created = created;
    }

    /**
    * 创建人id
    */
    private void setCreateId(Long createId){
    this.createId = createId;
    }

    /**
    * 修改时间，默认系统时间
    */
    private void setUpdated(Date updated){
    this.updated = updated;
    }

    /**
    * 修改人id
    */
    private void setUpdateId(Long updateId){
    this.updateId = updateId;
    }

    /**
    * 是否删除 0-未删除 ， 1-已删除
    */
    private void setIsDelete(Integer isDelete){
    this.isDelete = isDelete;
    }

    /**
    * 备用字段1
    */
    private void setExtend1(String extend1){
    this.extend1 = extend1;
    }

    /**
    * 备用字段2
    */
    private void setExtend2(String extend2){
    this.extend2 = extend2;
    }

    /**
    * 备用字段3
    */
    private void setExtend3(String extend3){
    this.extend3 = extend3;
    }

    /**
    * 备用字段4
    */
    private void setExtend4(String extend4){
    this.extend4 = extend4;
    }


    /**
    * 主键
    */
    private Long getId(){
    return this.id;
    }

    /**
    * 客户id，对应client_business表
    */
    private Long getClientId(){
    return this.clientId;
    }

    /**
    * 用户余额（单位：厘）
    */
    private Long getBalance(){
    return this.balance;
    }

    /**
    * 创建时间，默认系统时间
    */
    private Date getCreated(){
    return this.created;
    }

    /**
    * 创建人id
    */
    private Long getCreateId(){
    return this.createId;
    }

    /**
    * 修改时间，默认系统时间
    */
    private Date getUpdated(){
    return this.updated;
    }

    /**
    * 修改人id
    */
    private Long getUpdateId(){
    return this.updateId;
    }

    /**
    * 是否删除 0-未删除 ， 1-已删除
    */
    private Integer getIsDelete(){
    return this.isDelete;
    }

    /**
    * 备用字段1
    */
    private String getExtend1(){
    return this.extend1;
    }

    /**
    * 备用字段2
    */
    private String getExtend2(){
    return this.extend2;
    }

    /**
    * 备用字段3
    */
    private String getExtend3(){
    return this.extend3;
    }

    /**
    * 备用字段4
    */
    private String getExtend4(){
    return this.extend4;
    }

}
