package com.personal.management.service;

import com.personal.management.entity.SmsUser;

/**
 * 用户表(SmsUser)表服务接口
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
public interface SmsUserService {

    SmsUser queryByUsername(String username);
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SmsUser queryById(Long id);

    /**
     * 新增数据
     *
     * @param smsUser 实例对象
     * @return 实例对象
     */
    SmsUser insert(SmsUser smsUser);

    /**
     * 修改数据
     *
     * @param smsUser 实例对象
     * @return 实例对象
     */
    SmsUser update(SmsUser smsUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
