package com.personal.test.mapper;

import com.personal.test.entity.ClientBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName ClientBusinessMapper
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@Mapper
public interface ClientBusinessMapper {
    @Select("select * from client_business where id = #{id}")
    ClientBusiness findById(@Param("id") Long id);
}
