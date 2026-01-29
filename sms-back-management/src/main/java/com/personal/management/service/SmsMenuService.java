package com.personal.management.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.management.entity.SmsMenu;

import java.util.List;
import java.util.Map;

/**
 * 菜单表(SmsMenu)表服务接口
 *
 * @author makejava
 * @since 2026-01-27 19:06:38
 */
public interface SmsMenuService extends IService<SmsMenu> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SmsMenu queryById(Long id);

    /**
     * 新增数据
     *
     * @param smsMenu 实例对象
     * @return 实例对象
     */
    SmsMenu insert(SmsMenu smsMenu);

    /**
     * 修改数据
     *
     * @param smsMenu 实例对象
     * @return 实例对象
     */
    SmsMenu update(SmsMenu smsMenu);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Map<String, Object>> getMenuListByUserId(Long id);
}
