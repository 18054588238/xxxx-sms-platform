package com.personal.management.service.impl;

import com.personal.management.entity.SmsUserRole;
import com.personal.management.dao.SmsUserRoleDao;
import com.personal.management.service.SmsUserRoleService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 用户角色关系表(SmsUserRole)表服务实现类
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Service("smsUserRoleService")
public class SmsUserRoleServiceImpl implements SmsUserRoleService {
    @Resource
    private SmsUserRoleDao smsUserRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public SmsUserRole queryById(Long userId) {
        return this.smsUserRoleDao.queryById(userId);
    }

    /**
     * 新增数据
     *
     * @param smsUserRole 实例对象
     * @return 实例对象
     */
    @Override
    public SmsUserRole insert(SmsUserRole smsUserRole) {
        this.smsUserRoleDao.insert(smsUserRole);
        return smsUserRole;
    }

    /**
     * 修改数据
     *
     * @param smsUserRole 实例对象
     * @return 实例对象
     */
    @Override
    public SmsUserRole update(SmsUserRole smsUserRole) {
        this.smsUserRoleDao.update(smsUserRole);
        return this.queryById(smsUserRole.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long userId) {
        return this.smsUserRoleDao.deleteById(userId) > 0;
    }
}
