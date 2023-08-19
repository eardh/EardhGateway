package com.eardh.gateway.center.repository.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eardh.gateway.center.mapper.rpc.MicroserviceMapper;
import com.eardh.gateway.center.mapper.rpc.dubbo.InterfaceMapper;
import com.eardh.gateway.center.mapper.rpc.dubbo.MethodMapper;
import com.eardh.gateway.center.mapper.rpc.rest.RestInterfaceMapper;
import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.aggregator.DubboRoute;
import com.eardh.gateway.center.model.aggregator.RestRoute;
import com.eardh.gateway.center.model.rpc.MicroService;
import com.eardh.gateway.center.model.rpc.dubbo.DubboInterface;
import com.eardh.gateway.center.model.rpc.dubbo.DubboMethod;
import com.eardh.gateway.center.model.rpc.rest.RestInterface;
import com.eardh.gateway.center.model.rpc.vo.MicroServiceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboInterfaceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboMethodVO;
import com.eardh.gateway.center.model.rpc.vo.RestInterfaceVO;
import com.eardh.gateway.center.repository.IRpcManageRepository;
import com.eardh.gateway.common.model.RedisKey;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author eardh
 * @date 2023/3/30 9:35
 */
@Repository
public class RpcManageRepository implements IRpcManageRepository {

    @Resource
    private MicroserviceMapper microserviceMapper;

    @Resource
    private InterfaceMapper interfaceMapper;

    @Resource
    private MethodMapper methodMapper;

    @Resource
    private RestInterfaceMapper restInterfaceMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public MicroServiceVO registerMicroservice(MicroServiceVO microServiceVO) {
        MicroService microService = BeanUtil.toBean(microServiceVO, MicroService.class);
        if (StrUtil.isNotBlank(microServiceVO.getMicroserviceId())) {
            MicroService microService1 = microserviceMapper.selectById(microService.getMicroserviceId());
            microserviceMapper.updateById(microService);
            checkApplicationUpdateIfPublishMessage(microService, microService1);
        } else {
            LambdaQueryWrapper<MicroService> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MicroService::getMicroserviceName, microServiceVO.getMicroserviceName())
                    .eq(MicroService::getMicroserviceGroup, microServiceVO.getMicroserviceGroup());
            MicroService one = microserviceMapper.selectOne(queryWrapper);
            if (ObjectUtil.isNull(one)) {
                microserviceMapper.insert(microService);
            } else {
                microService.setMicroserviceId(one.getMicroserviceId());
                microserviceMapper.updateById(microService);
                checkApplicationUpdateIfPublishMessage(microService, one);
            }
        }
        return BeanUtil.toBean(microService, MicroServiceVO.class);
    }

    private void checkApplicationUpdateIfPublishMessage(MicroService app1, MicroService app2) {
        if (ObjectUtil.isAllNotEmpty(app1, app2) && !ObjectUtil.equals(app1, app2)) {
            List<DubboRoute> routes = methodMapper.selectByMicroserviceIdIfInterfaceId(app1.getMicroserviceId(), null);
            for (DubboRoute route : routes) {
                redisTemplate.convertAndSend(RedisKey.GATEWAY_DUBBO_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(route));
            }
            List<RestRoute> restRoutes = restInterfaceMapper.selectByMicroserviceId(app1.getMicroserviceId());
            for (RestRoute restRoute : restRoutes) {
                redisTemplate.convertAndSend(RedisKey.GATEWAY_REST_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(restRoute));
            }
        }
    }

    @Override
    public PageVO<MicroServiceVO> getMicroservices(Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<MicroService> queryWrapper = new LambdaQueryWrapper<>();
        Page<MicroService> page = new Page<>(currentPage, pageSize);
        Page<MicroService> applicationPage = microserviceMapper.selectPage(page, queryWrapper);
        return new PageVO<>(applicationPage, MicroServiceVO.class);
    }

    @Override
    public DubboInterfaceVO registerDubboInterface(DubboInterfaceVO dubboInterfaceVO) {
        DubboInterface anDubboInterface = BeanUtil.toBean(dubboInterfaceVO, DubboInterface.class);
        if (StrUtil.isNotBlank(dubboInterfaceVO.getInterfaceId())) {
            DubboInterface dubboInterface = interfaceMapper.selectById(anDubboInterface.getInterfaceId());
            interfaceMapper.updateById(anDubboInterface);
            checkInterfaceUpdateIfPublishMessage(anDubboInterface, dubboInterface);
        } else {
            LambdaQueryWrapper<DubboInterface> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(DubboInterface::getInterfaceId)
                    .eq(DubboInterface::getMicroserviceId, dubboInterfaceVO.getMicroserviceId())
                    .eq(DubboInterface::getInterfaceName, dubboInterfaceVO.getInterfaceName())
                    .eq(DubboInterface::getInterfaceGroup, dubboInterfaceVO.getInterfaceGroup());
            DubboInterface one = interfaceMapper.selectOne(queryWrapper);
            if (ObjectUtil.isNull(one)) {
                interfaceMapper.insert(anDubboInterface);
            } else {
                anDubboInterface.setInterfaceId(one.getInterfaceId());
                interfaceMapper.updateById(anDubboInterface);
                checkInterfaceUpdateIfPublishMessage(anDubboInterface, one);
            }
        }
        return BeanUtil.toBean(anDubboInterface, DubboInterfaceVO.class);
    }

    private void checkInterfaceUpdateIfPublishMessage(DubboInterface int1, DubboInterface int2) {
        if (ObjectUtil.isAllNotEmpty(int1, int2) && !ObjectUtil.equals(int1, int2)) {
            List<DubboRoute> routes = methodMapper.selectByMicroserviceIdIfInterfaceId(int1.getMicroserviceId(), int1.getInterfaceId());
            for (DubboRoute route : routes) {
                redisTemplate.convertAndSend(RedisKey.GATEWAY_DUBBO_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(route));
            }
        }
    }

    @Override
    public DubboMethodVO registerDubboMethod(DubboMethodVO dubboMethodVO) {
        DubboMethod dubboMethod = BeanUtil.toBean(dubboMethodVO, DubboMethod.class);
        if (StrUtil.isNotBlank(dubboMethodVO.getMethodId())) {
            DubboMethod one = methodMapper.selectById(dubboMethod.getMethodId());
            methodMapper.updateById(dubboMethod);
            checkDubboRouteUpdateIfPublishMessage(dubboMethod, one);
        } else {
            LambdaQueryWrapper<DubboMethod> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DubboMethod::getMicroserviceId, dubboMethod.getMicroserviceId())
                    .eq(DubboMethod::getInterfaceId, dubboMethod.getInterfaceId())
                    .eq(DubboMethod::getMethodName, dubboMethod.getMethodName())
                    .eq(ObjectUtil.isNotNull(dubboMethod.getMethodParametersType()), DubboMethod::getMethodParametersType, dubboMethod.getMethodParametersType());
            DubboMethod one = methodMapper.selectOne(queryWrapper);
            if (ObjectUtil.isNull(one)) {
                methodMapper.insert(dubboMethod);
                if (StrUtil.isNotBlank(dubboMethodVO.getApiId())) {
                    DubboRoute route = methodMapper.selectByApiId(dubboMethod.getApiId());
                    redisTemplate.convertAndSend(RedisKey.GATEWAY_DUBBO_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(route));
                }
            } else {
                dubboMethod.setMethodId(one.getMethodId());
                methodMapper.updateById(dubboMethod);
                checkDubboRouteUpdateIfPublishMessage(dubboMethod, one);
            }
        }
        return BeanUtil.toBean(dubboMethod, DubboMethodVO.class);
    }

    /**
     * @param m1 更新的对象
     * @param m2 更新前的对象
     */
    private void checkDubboRouteUpdateIfPublishMessage(DubboMethod m1, DubboMethod m2) {
        if (!ObjectUtil.equals(m1, m2)) {
            if (ObjectUtil.isNull(m1.getApiId())) {
                if (ObjectUtil.isNotNull(m2.getApiId())) {
                    DubboRoute dubboRoute = new DubboRoute();
                    dubboRoute.setApiId(String.valueOf(m2.getApiId()));
                    redisTemplate.convertAndSend(RedisKey.GATEWAY_DUBBO_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(dubboRoute));
                }
            } else {
                DubboRoute dubboRoute = methodMapper.selectByApiId(m1.getApiId());
                if (ObjectUtil.isNotNull(dubboRoute)) {
                    redisTemplate.convertAndSend(RedisKey.GATEWAY_DUBBO_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(dubboRoute));
                }
            }
        }
    }


    public PageVO<DubboRoute> getDubboRoutes(Integer currentPage, Integer pageSize, Boolean requireApiId) {
        Page<DubboRoute> page = new Page<>(currentPage, pageSize);
        Page<DubboRoute> routePage = methodMapper.selectPage(page, requireApiId);
        return new PageVO<>(routePage, DubboRoute.class);
    }

    @Override
    public DubboRoute getDubboRoute(Long apiId) {
        return methodMapper.selectByApiId(apiId);
    }

    @Override
    public MicroServiceVO getApplication(String microserviceId) {
        MicroService microService = microserviceMapper.selectById(microserviceId);
        return BeanUtil.toBean(microService, MicroServiceVO.class);
    }

    @Override
    public RestInterfaceVO registerRestInterface(RestInterfaceVO restInterfaceVO) {
        RestInterface restInterface = BeanUtil.toBean(restInterfaceVO, RestInterface.class);
        if (StrUtil.isNotBlank(restInterfaceVO.getId())) {
            RestInterface one = restInterfaceMapper.selectById(restInterface.getId());
            restInterfaceMapper.updateById(restInterface);
            checkRestRouteUpdateIfPublishMessage(restInterface, one);
        } else {
            LambdaQueryWrapper<RestInterface> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RestInterface::getMicroserviceId, restInterface.getMicroserviceId())
                    .eq(RestInterface::getRestUrl, restInterface.getRestUrl())
                    .eq(RestInterface::getRestMethod, restInterface.getRestMethod());
            RestInterface one = restInterfaceMapper.selectOne(queryWrapper);
            if (ObjectUtil.isNull(one)) {
                restInterfaceMapper.insert(restInterface);
                if (StrUtil.isNotBlank(restInterfaceVO.getApiId())) {
                    RestRoute restRoute = restInterfaceMapper.selectByApiId(restInterface.getApiId());
                    redisTemplate.convertAndSend(RedisKey.GATEWAY_REST_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(restRoute));
                }
            } else {
                restInterface.setId(one.getId());
                restInterfaceMapper.updateById(restInterface);
                checkRestRouteUpdateIfPublishMessage(restInterface, one);
            }
        }
        return BeanUtil.toBean(restInterface, RestInterfaceVO.class);
    }

    /**
     * @param r1 更新的对象
     * @param r2 更新前的对象
     */
    private void checkRestRouteUpdateIfPublishMessage(RestInterface r1, RestInterface r2) {
        if (!ObjectUtil.equals(r1, r2)) {
            if (ObjectUtil.isNull(r1.getApiId())) {
                if (ObjectUtil.isNotNull(r2.getApiId())) {
                    RestRoute restRoute = new RestRoute();
                    restRoute.setApiId(String.valueOf(r2.getApiId()));
                    redisTemplate.convertAndSend(RedisKey.GATEWAY_REST_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(restRoute));
                }
            } else {
                RestRoute restRoute = restInterfaceMapper.selectByApiId(r1.getApiId());
                if (ObjectUtil.isNotNull(restRoute)) {
                    redisTemplate.convertAndSend(RedisKey.GATEWAY_REST_ROUTES_CHANNEL_KEY, JSONUtil.toJsonStr(restRoute));
                }
            }
        }
    }

    @Override
    public PageVO<RestRoute> getRestRoutes(Integer currentPage, Integer pageSize, Boolean requireApiId) {
        Page<RestRoute> page = new Page<>(currentPage, pageSize);
        Page<RestRoute> routePage = restInterfaceMapper.selectPage(page, requireApiId);
        return new PageVO<>(routePage, RestRoute.class);
    }

    @Override
    public RestRoute getRestRoute(Long apiId) {
        return restInterfaceMapper.selectByApiId(apiId);
    }
}
