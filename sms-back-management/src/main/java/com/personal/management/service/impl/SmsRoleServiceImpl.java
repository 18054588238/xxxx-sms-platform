package com.personal.management.service.impl;

import com.personal.management.entity.SmsRole;
import com.personal.management.dao.SmsRoleDao;
import com.personal.management.service.SmsRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 角色表(SmsRole)表服务实现类
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Service("smsRoleService")
public class SmsRoleServiceImpl implements SmsRoleService {
    @Resource
    private SmsRoleDao smsRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SmsRole queryById(Long id) {
        return this.smsRoleDao.queryById(id);
    }


    /**
     * 新增数据
     *
     * @param smsRole 实例对象
     * @return 实例对象
     */
    @Override
    public SmsRole insert(SmsRole smsRole) {
        this.smsRoleDao.insert(smsRole);
        return smsRole;
    }

    /**
     * 修改数据
     *
     * @param smsRole 实例对象
     * @return 实例对象
     */
    @Override
    public SmsRole update(SmsRole smsRole) {
        this.smsRoleDao.update(smsRole);
        return this.queryById(smsRole.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.smsRoleDao.deleteById(id) > 0;
    }
}
