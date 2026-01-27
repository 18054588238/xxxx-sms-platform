package com.personal.management.service;

import com.personal.management.entity.SmsRole;

/**
 * 角色表(SmsRole)表服务接口
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
public interface SmsRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SmsRole queryById(Integer id);


    /**
     * 新增数据
     *
     * @param smsRole 实例对象
     * @return 实例对象
     */
    SmsRole insert(SmsRole smsRole);

    /**
     * 修改数据
     *
     * @param smsRole 实例对象
     * @return 实例对象
     */
    SmsRole update(SmsRole smsRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
