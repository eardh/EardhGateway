package com.eardh.gateway.center.service;


import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.gateway.vo.GatewayServerVO;

/**
 * 网关配置服务
 * @author eardh
 * @date 2023/3/23 11:18
 */
public interface IGatewayManageService {

    GatewayServerVO registerGatewayServer(GatewayServerVO gatewayServerVO);

    PageVO<GatewayServerVO> getGatewayServers(Integer currentPage, Integer pageSize);

    GatewayServerVO getGatewayServer(String gatewayId);
}
