package com.personal.management.dao;

import com.personal.management.entity.SmsMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 菜单表(SmsMenu)表数据库访问层
 *
 * @author makejava
 * @since 2026-01-27 19:06:33
 */
@Mapper
public interface SmsMenuDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SmsMenu queryById(Integer id);

    /**
     * 统计总行数
     *
     * @param smsMenu 查询条件
     * @return 总行数
     */
    long count(SmsMenu smsMenu);

    /**
     * 新增数据
     *
     * @param smsMenu 实例对象
     * @return 影响行数
     */
    int insert(SmsMenu smsMenu);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SmsMenu> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SmsMenu> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SmsMenu> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SmsMenu> entities);

    /**
     * 修改数据
     *
     * @param smsMenu 实例对象
     * @return 影响行数
     */
    int update(SmsMenu smsMenu);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

