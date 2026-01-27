package com.personal.management.service.impl;

import com.personal.management.entity.SmsMenu;
import com.personal.management.dao.SmsMenuDao;
import com.personal.management.service.SmsMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 菜单表(SmsMenu)表服务实现类
 *
 * @author makejava
 * @since 2026-01-27 19:06:38
 */
@Service("smsMenuService")
public class SmsMenuServiceImpl implements SmsMenuService {
    @Resource
    private SmsMenuDao smsMenuDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SmsMenu queryById(Integer id) {
        return this.smsMenuDao.queryById(id);
    }


    /**
     * 新增数据
     *
     * @param smsMenu 实例对象
     * @return 实例对象
     */
    @Override
    public SmsMenu insert(SmsMenu smsMenu) {
        this.smsMenuDao.insert(smsMenu);
        return smsMenu;
    }

    /**
     * 修改数据
     *
     * @param smsMenu 实例对象
     * @return 实例对象
     */
    @Override
    public SmsMenu update(SmsMenu smsMenu) {
        this.smsMenuDao.update(smsMenu);
        return this.queryById(smsMenu.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.smsMenuDao.deleteById(id) > 0;
    }
}
