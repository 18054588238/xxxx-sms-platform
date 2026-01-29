package com.personal.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.management.dao.ClientBusinessMapper;
import com.personal.management.entity.ClientBusiness;
import com.personal.management.service.ClientBusinessService;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ClientBusinessServiceImpl
 * @Author liupanpan
 * @Date 2026/1/28
 * @Description
 */
@Service
public class ClientBusinessServiceImpl extends ServiceImpl<ClientBusinessMapper, ClientBusiness> implements ClientBusinessService {
    @Override
    public List<ClientBusiness> queryAllClientBusiness(Long id) {
        List<ClientBusiness> businessList = this.list().stream()
                .filter(i -> i.getIsDelete() == 0)
                .collect(Collectors.toList());

        if (id == 1) {
            // 超级管理员，返回所有数据
            return businessList;
        }
        return businessList.stream()
                .filter(i -> StringUtils.isNotBlank(i.getExtend1()) && i.getExtend1().equals(id.toString()))
                .collect(Collectors.toList());
    }
}
