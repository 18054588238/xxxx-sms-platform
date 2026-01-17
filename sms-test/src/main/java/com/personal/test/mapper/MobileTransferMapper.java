package com.personal.test.mapper;

import com.personal.test.entity.MobileTransfer;
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
public interface MobileTransferMapper {
    @Select("select transfer_number,now_isp from mobile_transfer where is_transfer = 1 and is_delete = 0")
    List<MobileTransfer> getMobileTransfer();
}
