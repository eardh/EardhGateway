package com.eardh.gateway.center.repository;


import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.gateway.vo.GatewayServerVO;

/**
 * @author eardh
 * @date 2023/4/8 14:33
 */
public interface IGatewayManageRepository {

    GatewayServerVO registerGatewayNode(GatewayServerVO gatewayServerVO);

    PageVO<GatewayServerVO> getGatewayServers(Integer currentPage, Integer pageSize);

    GatewayServerVO getGatewayServer(String gatewayId);
}
