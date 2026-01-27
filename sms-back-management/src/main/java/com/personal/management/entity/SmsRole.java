package com.personal.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 角色表(SmsRole)实体类
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRole implements Serializable {
    private static final long serialVersionUID = 370387353989806037L;

    private Integer id;
/**
     * 角色名
     */
    private String name;
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

}

