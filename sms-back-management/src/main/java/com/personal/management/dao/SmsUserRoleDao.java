package com.personal.management.dao;

import com.personal.management.entity.SmsUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户角色关系表(SmsUserRole)表数据库访问层
 *
 * @author makejava
 * @since 2026-01-27 19:06:40
 */
@Mapper
public interface SmsUserRoleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    SmsUserRole queryById(Long userId);

    /**
     * 统计总行数
     *
     * @param smsUserRole 查询条件
     * @return 总行数
     */
    long count(SmsUserRole smsUserRole);

    /**
     * 新增数据
     *
     * @param smsUserRole 实例对象
     * @return 影响行数
     */
    int insert(SmsUserRole smsUserRole);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SmsUserRole> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SmsUserRole> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SmsUserRole> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SmsUserRole> entities);

    /**
     * 修改数据
     *
     * @param smsUserRole 实例对象
     * @return 影响行数
     */
    int update(SmsUserRole smsUserRole);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 影响行数
     */
    int deleteById(Long userId);

}

