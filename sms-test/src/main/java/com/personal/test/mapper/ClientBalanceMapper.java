package com.personal.test.mapper;

import com.personal.test.entity.ClientBalance;
import com.personal.test.entity.ClientBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName ClientBalanceMapper
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@Mapper
public interface ClientBalanceMapper {

    @Select("select * from client_balance where client_id = #{clientId}")
    ClientBalance findByClientId(@Param("clientId") Long clientId);
}
