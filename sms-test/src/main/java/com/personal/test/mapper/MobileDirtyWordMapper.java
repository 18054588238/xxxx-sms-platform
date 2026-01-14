package com.personal.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName MobileDirtyWordMapper
 * @Author liupanpan
 * @Date 2026/1/14
 * @Description
 */
@Mapper
public interface MobileDirtyWordMapper {
    @Select("select dirtyword from mobile_dirtyword")
    List<String> getDirtyWord();
}
