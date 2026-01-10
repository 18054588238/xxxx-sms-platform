package com.personal.test.mapper;

import com.personal.test.entity.ClientSign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName ClientSignMapper
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@Mapper
public interface ClientSignMapper {
    @Select("select * from client_sign where client_id = #{clientId}")
    List<ClientSign> findByClientId(@Param("clientId") Long clientId);
}
