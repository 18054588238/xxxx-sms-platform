package com.personal.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.management.entity.SmsUserRole;

/**
 * 用户角色关系表(SmsUserRole)表服务接口
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
public interface SmsUserRoleService extends IService<SmsUserRole> {

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    SmsUserRole queryById(Long userId);

    /**
     * 新增数据
     *
     * @param smsUserRole 实例对象
     * @return 实例对象
     */
    SmsUserRole insert(SmsUserRole smsUserRole);

    /**
     * 修改数据
     *
     * @param smsUserRole 实例对象
     * @return 实例对象
     */
    SmsUserRole update(SmsUserRole smsUserRole);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Long userId);

}
