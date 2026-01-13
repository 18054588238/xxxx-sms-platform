package com.personal.test.mapper;

import com.personal.test.entity.ClientTemplate;
import com.personal.test.entity.MobileArea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName MobileAreaMapper
 * @Author liupanpan
 * @Date 2026/1/12
 * @Description
 */
@Mapper
public interface MobileAreaMapper {
    @Select("select mobile_number,mobile_area,mobile_type from mobile_area")
    List<MobileArea> findAll();
}
