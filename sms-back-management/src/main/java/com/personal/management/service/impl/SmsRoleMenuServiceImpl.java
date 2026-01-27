package com.personal.management.service.impl;

import com.personal.management.entity.SmsRoleMenu;
import com.personal.management.dao.SmsRoleMenuDao;
import com.personal.management.service.SmsRoleMenuService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 用户角色关系表(SmsRoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Service("smsRoleMenuService")
public class SmsRoleMenuServiceImpl implements SmsRoleMenuService {
    @Resource
    private SmsRoleMenuDao smsRoleMenuDao;

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    @Override
    public SmsRoleMenu queryById(Long roleId) {
        return this.smsRoleMenuDao.queryById(roleId);
    }

    /**
     * 新增数据
     *
     * @param smsRoleMenu 实例对象
     * @return 实例对象
     */
    @Override
    public SmsRoleMenu insert(SmsRoleMenu smsRoleMenu) {
        this.smsRoleMenuDao.insert(smsRoleMenu);
        return smsRoleMenu;
    }

    /**
     * 修改数据
     *
     * @param smsRoleMenu 实例对象
     * @return 实例对象
     */
    @Override
    public SmsRoleMenu update(SmsRoleMenu smsRoleMenu) {
        this.smsRoleMenuDao.update(smsRoleMenu);
        return this.queryById(smsRoleMenu.getRoleId());
    }

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long roleId) {
        return this.smsRoleMenuDao.deleteById(roleId) > 0;
    }
}
