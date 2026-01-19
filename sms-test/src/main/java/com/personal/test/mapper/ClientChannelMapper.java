package com.personal.test.mapper;

import com.personal.test.entity.ClientBalance;
import com.personal.test.entity.ClientChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName ClientBalanceMapper
 * @Author liupanpan
 * @Date 2026/1/10
 * @Description
 */
@Mapper
public interface ClientChannelMapper {

    @Select("select * from client_channel where is_available = 1 and is_delete = 0")
    List<ClientChannel> findAll();
}
