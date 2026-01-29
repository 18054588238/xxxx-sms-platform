package com.personal.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.management.entity.ClientBusiness;

import java.util.List;

/**
 * @ClassName ClientBusinessService
 * @Author liupanpan
 * @Date 2026/1/28
 * @Description
 */
public interface ClientBusinessService extends IService<ClientBusiness> {
    List<ClientBusiness> queryAllClientBusiness(Long id);
}
