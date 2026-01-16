package com.personal.test.mapper;

import com.personal.test.entity.MobileBlack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName MobileBlackMapper
 * @Author liupanpan
 * @Date 2026/1/16
 * @Description
 */
@Mapper
public interface MobileBlackMapper {
    @Select("select black_number,client_id from mobile_black where is_delete = 0")
    List<MobileBlack> getMobileBlackList();
}
