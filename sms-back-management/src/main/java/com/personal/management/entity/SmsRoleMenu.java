package com.personal.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 用户角色关系表(SmsRoleMenu)实体类
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRoleMenu implements Serializable {
    private static final long serialVersionUID = 932309251236307290L;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 菜单id
     */
    private Long menuId;
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

}