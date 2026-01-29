package com.personal.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.MapStructUtil;
import com.personal.management.dao.SmsUserRoleDao;
import com.personal.management.entity.SmsMenu;
import com.personal.management.dao.SmsMenuDao;
import com.personal.management.entity.SmsRoleMenu;
import com.personal.management.entity.SmsUserRole;
import com.personal.management.service.SmsMenuService;
import com.personal.management.service.SmsRoleMenuService;
import com.personal.management.service.SmsUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单表(SmsMenu)表服务实现类
 *
 * @author makejava
 * @since 2026-01-27 19:06:38
 */
@Service("smsMenuService")
public class SmsMenuServiceImpl extends ServiceImpl<SmsMenuDao,SmsMenu> implements SmsMenuService {
    @Resource
    private SmsMenuDao smsMenuDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SmsMenu queryById(Long id) {
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

    @Override
    public List<Map<String, Object>> getMenuListByUserId(Long userId) {

        // 先根据用户id查询所有权限下的菜单数据
        List<Map<String, Object>> list = smsMenuDao.getMenuListByUserId(userId);
        /*List<Long> roleIds = userRoleService.list().stream()
                .filter(i -> i.getUserId().equals(userId) && i.getIsDelete().equals(0))
                .map(SmsUserRole::getRoleId)
                .collect(Collectors.toList());

        List<Long> menuIds = roleMenuService.list().stream()
                .filter(i -> roleIds.contains(i.getRoleId()))
                .map(SmsRoleMenu::getMenuId)
                .collect(Collectors.toList());

        List<SmsMenu> menuList = menuService.listByIds(menuIds);*/
        // 封装为树形结构
        // 2、封装外层的父级菜单封装到当前的List集合
        List<Map<String, Object>> data = new ArrayList<>();
        //3、使用迭代器遍历所有的菜单信息，封装父级菜单
        ListIterator<Map<String, Object>> parentIterator = list.listIterator();
        while (parentIterator.hasNext()) {
            Map<String, Object> menu = parentIterator.next();
            if ((int) menu.get("type") == 0) {
                // 是父级菜单
                data.add(menu);
                parentIterator.remove();
            } else {
                break;
            }
        }
        //4、存放二级菜单
        for (Map<String, Object> parentMenu : data) {
            List<Map<String, Object>> sonMenuList = new ArrayList<>();
            ListIterator<Map<String, Object>> sonIterator = list.listIterator();
            while (sonIterator.hasNext()) {
                Map<String, Object> sonMenu = sonIterator.next();
                if ((long) parentMenu.get("id") == (long) sonMenu.get("parentId")) {
                    sonMenuList.add(sonMenu);
                    sonIterator.remove();
                }
            }
            parentMenu.put("list", sonMenuList);
        }
        //5、返回data
        return data;
    }
}
