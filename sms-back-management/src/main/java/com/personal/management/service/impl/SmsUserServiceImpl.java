package com.personal.management.service.impl;

import com.personal.management.entity.SmsUser;
import com.personal.management.dao.SmsUserDao;
import com.personal.management.service.SmsUserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 用户表(SmsUser)表服务实现类
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Service("smsUserService")
public class SmsUserServiceImpl implements SmsUserService {
    @Resource
    private SmsUserDao smsUserDao;

    @Override
    public SmsUser queryByUsername(String username) {
        return smsUserDao.queryByUsername(username);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SmsUser queryById(Integer id) {
        return this.smsUserDao.queryById(id);
    }


    /**
     * 新增数据
     *
     * @param smsUser 实例对象
     * @return 实例对象
     */
    @Override
    public SmsUser insert(SmsUser smsUser) {
        this.smsUserDao.insert(smsUser);
        return smsUser;
    }

    /**
     * 修改数据
     *
     * @param smsUser 实例对象
     * @return 实例对象
     */
    @Override
    public SmsUser update(SmsUser smsUser) {
        this.smsUserDao.update(smsUser);
        return this.queryById(smsUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.smsUserDao.deleteById(id) > 0;
    }
}
