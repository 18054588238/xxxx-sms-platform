package com.personal.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 菜单表(SmsMenu)实体类
 *
 * @author makejava
 * @since 2026-01-27 19:06:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsMenu implements Serializable {
    private static final long serialVersionUID = -89754391551545977L;

    private Long id;
/**
     * 菜单名
     */
    private String name;
/**
     * 父菜单id
     */
    private Long parentId;
/**
     * 跳转的连接地址
     */
    private String url;
/**
     * 按钮的小图标
     */
    private String icon;
/**
     * 菜单的类型
     */
    private Integer type;
/**
     * 菜单排序规则
     */
    private Integer sort;
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

