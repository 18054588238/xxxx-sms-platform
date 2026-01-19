package com.personal.test.mapper;

import com.personal.test.entity.Channel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName ChanelMapper
 * @Author liupanpan
 * @Date 2026/1/18
 * @Description
 */
@Mapper
public interface ChannelMapper {
    @Select("select * from channel where is_available = 1 and is_delete = 0")
    List<Channel> findAll();
}
