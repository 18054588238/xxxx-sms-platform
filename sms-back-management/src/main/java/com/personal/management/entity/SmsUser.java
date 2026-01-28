package com.personal.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(SmsUser)实体类
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsUser implements Serializable {
    private static final long serialVersionUID = 500871657943932164L;

    private Long id;
/**
     * 用户名
     */
    private String username;
/**
     * 用户登录密码
     */
    private String password;
/**
     * 认证的盐
     */
    private String salt;
/**
     * 昵称
     */
    private String nickname;
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

}

