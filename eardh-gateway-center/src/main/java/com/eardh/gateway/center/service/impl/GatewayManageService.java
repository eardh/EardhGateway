package com.eardh.gateway.center.service.impl;

import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.gateway.vo.GatewayServerVO;
import com.eardh.gateway.center.repository.IGatewayManageRepository;
import com.eardh.gateway.center.service.IGatewayManageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author eardh
 * @date 2023/3/23 11:20
 */
@Service
public class GatewayManageService implements IGatewayManageService {

    @Resource
    private IGatewayManageRepository manageRepository;

    @Override
    public GatewayServerVO registerGatewayServer(GatewayServerVO gatewayServerVO) {
        return manageRepository.registerGatewayNode(gatewayServerVO);
    }

    @Override
    public PageVO<GatewayServerVO> getGatewayServers(Integer currentPage, Integer pageSize) {
        return manageRepository.getGatewayServers(currentPage, pageSize);
    }

    @Override
    public GatewayServerVO getGatewayServer(String gatewayId) {
        return manageRepository.getGatewayServer(gatewayId);
    }
}
