package com.personal.test.mapper;

import com.personal.test.entity.ClientTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName ClientTemplateMapper
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@Mapper
public interface ClientTemplateMapper {
    @Select("select * from client_template where sign_id = #{signId}")
    List<ClientTemplate> findBySignId(@Param("signId") Long signId);
}
