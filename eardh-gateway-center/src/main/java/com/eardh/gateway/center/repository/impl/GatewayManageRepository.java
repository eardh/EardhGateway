package com.eardh.gateway.center.repository.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eardh.gateway.center.mapper.gateway.GatewayServerMapper;
import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.gateway.GatewayServer;
import com.eardh.gateway.center.model.gateway.vo.GatewayServerVO;
import com.eardh.gateway.center.repository.IGatewayManageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

/**
 * @author eardh
 * @date 2023/4/8 14:35
 */
@Repository
public class GatewayManageRepository implements IGatewayManageRepository {

    @Resource
    private GatewayServerMapper gatewayServerMapper;

    @Override
    public GatewayServerVO registerGatewayNode(GatewayServerVO gatewayServerVO) {
        GatewayServer bean = BeanUtil.toBean(gatewayServerVO, GatewayServer.class);
        if (StrUtil.isNotBlank(gatewayServerVO.getGatewayId())) {
            gatewayServerMapper.updateById(bean);
        } else {
            LambdaQueryWrapper<GatewayServer> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GatewayServer::getGatewayName, gatewayServerVO.getGatewayName())
                    .eq(GatewayServer::getGroupName, gatewayServerVO.getGroupName());
            GatewayServer gatewayServer = gatewayServerMapper.selectOne(wrapper);
            if (ObjectUtil.isNull(gatewayServer)) {
                gatewayServerMapper.insert(bean);
            } else {
                bean.setGatewayId(gatewayServer.getGatewayId());
                gatewayServerMapper.updateById(bean);
            }
        }
        return BeanUtil.toBean(bean, GatewayServerVO.class);
    }

    @Override
    public PageVO<GatewayServerVO> getGatewayServers(Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<GatewayServer> queryWrapper = new LambdaQueryWrapper<>();
        Page<GatewayServer> page = new Page<>(currentPage, pageSize);
        Page<GatewayServer> serverPage = gatewayServerMapper.selectPage(page, queryWrapper);
        return new PageVO<>(serverPage, GatewayServerVO.class);
    }

    @Override
    public GatewayServerVO getGatewayServer(String gatewayId) {
        GatewayServer gatewayServer = gatewayServerMapper.selectById(gatewayId);
        return BeanUtil.toBean(gatewayServer, GatewayServerVO.class);
    }
}
