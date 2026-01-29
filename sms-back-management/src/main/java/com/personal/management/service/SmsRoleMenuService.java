package com.personal.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.management.entity.SmsRoleMenu;

/**
 * 用户角色关系表(SmsRoleMenu)表服务接口
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
public interface SmsRoleMenuService extends IService<SmsRoleMenu> {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    SmsRoleMenu queryById(Long roleId);

    /**
     * 新增数据
     *
     * @param smsRoleMenu 实例对象
     * @return 实例对象
     */
    SmsRoleMenu insert(SmsRoleMenu smsRoleMenu);

    /**
     * 修改数据
     *
     * @param smsRoleMenu 实例对象
     * @return 实例对象
     */
    SmsRoleMenu update(SmsRoleMenu smsRoleMenu);

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    boolean deleteById(Long roleId);

}
