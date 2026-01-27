package com.personal.management.dao;

import com.personal.management.entity.SmsRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户角色关系表(SmsRoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Mapper
public interface SmsRoleMenuDao {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    SmsRoleMenu queryById(Long roleId);

    /**
     * 统计总行数
     *
     * @param smsRoleMenu 查询条件
     * @return 总行数
     */
    long count(SmsRoleMenu smsRoleMenu);

    /**
     * 新增数据
     *
     * @param smsRoleMenu 实例对象
     * @return 影响行数
     */
    int insert(SmsRoleMenu smsRoleMenu);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SmsRoleMenu> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SmsRoleMenu> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SmsRoleMenu> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SmsRoleMenu> entities);

    /**
     * 修改数据
     *
     * @param smsRoleMenu 实例对象
     * @return 影响行数
     */
    int update(SmsRoleMenu smsRoleMenu);

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 影响行数
     */
    int deleteById(Long roleId);

}

