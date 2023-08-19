package com.eardh.gateway.center.repository.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eardh.gateway.center.mapper.api.*;
import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.api.*;
import com.eardh.gateway.center.model.api.vo.*;
import com.eardh.gateway.center.repository.IApiManageRepository;
import com.eardh.gateway.common.model.RedisKey;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author eardh
 * @date 2023/4/3 11:55
 */
@Repository
public class ApiManageRepository implements IApiManageRepository {

    @Resource
    private GatewayApiMapper gatewayApiMapper;

    @Resource
    private ApiIpWhitelistMapper whitelistMapper;

    @Resource
    private ApiIpBlacklistMapper blacklistMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ApiRateLimiterMapper rateLimiterMapper;

    @Resource
    private ApiInvokeLogMapper invokeLogMapper;

    @Override
    public GatewayApiVO registerApi(GatewayApiVO gatewayApiVO) {
        GatewayApi gatewayApi = BeanUtil.toBean(gatewayApiVO, GatewayApi.class);
        LambdaQueryWrapper<GatewayApi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GatewayApi::getApiPath, gatewayApiVO.getApiPath())
                .eq(GatewayApi::getApiMethod, gatewayApi.getApiMethod());
        GatewayApi one = gatewayApiMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(one)) {
            gatewayApiMapper.insert(gatewayApi);
            redisTemplate.convertAndSend(RedisKey.GATEWAY_API_CHANNEL_KEY, JSONUtil.toJsonStr(gatewayApi));
        } else {
            gatewayApi.setApiId(one.getApiId());
            if (!ObjectUtil.equals(gatewayApi, one)) {
                redisTemplate.convertAndSend(RedisKey.GATEWAY_API_CHANNEL_KEY, JSONUtil.toJsonStr(gatewayApi));
                gatewayApiMapper.updateById(gatewayApi);
            }
        }
        return BeanUtil.toBean(gatewayApi, GatewayApiVO.class);
    }

    @Override
    public PageVO<GatewayApiVO> getApis(Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<GatewayApi> queryWrapper = new LambdaQueryWrapper<>();
        Page<GatewayApi> page = new Page<>(currentPage, pageSize);
        Page<GatewayApi> apiPage = gatewayApiMapper.selectPage(page, queryWrapper);
        return new PageVO<>(apiPage, GatewayApiVO.class);
    }

    @Override
    public PageVO<ApiIpWhitelistVO> getIpWhiteList(Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<ApiIpWhitelist> queryWrapper = new LambdaQueryWrapper<>();
        Page<ApiIpWhitelist> page = new Page<>(currentPage, pageSize);
        Page<ApiIpWhitelist> whitelistPage = whitelistMapper.selectPage(page, queryWrapper);
        return new PageVO<>(whitelistPage, ApiIpWhitelistVO.class);
    }

    @Override
    public PageVO<ApiIpBlacklistVO> getIpBlackList(Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<ApiIpBlacklist> queryWrapper = new LambdaQueryWrapper<>();
        Page<ApiIpBlacklist> page = new Page<>(currentPage, pageSize);
        Page<ApiIpBlacklist> blacklistPage = blacklistMapper.selectPage(page, queryWrapper);
        return new PageVO<>(blacklistPage, ApiIpBlacklistVO.class);
    }

    @Override
    public ApiIpWhitelistVO registerIpWhiteList(ApiIpWhitelistVO apiIpWhitelistVO) {
        ApiIpWhitelist whitelist = BeanUtil.toBean(apiIpWhitelistVO, ApiIpWhitelist.class);
        LambdaQueryWrapper<ApiIpWhitelist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiIpWhitelist::getApiId, apiIpWhitelistVO.getApiId());
        ApiIpWhitelist apiIpWhitelist = whitelistMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(apiIpWhitelist)) {
            whitelistMapper.insert(whitelist);
            redisTemplate.convertAndSend(RedisKey.GATEWAY_IP_WHITELIST_CHANNEL_KEY, JSONUtil.toJsonStr(whitelist));
        } else {
            whitelist.setId(apiIpWhitelist.getId());
            whitelistMapper.updateById(whitelist);
            if (!ObjectUtil.equals(whitelist, apiIpWhitelist)) {
                redisTemplate.convertAndSend(RedisKey.GATEWAY_IP_WHITELIST_CHANNEL_KEY, JSONUtil.toJsonStr(whitelist));
            }
        }
        return BeanUtil.toBean(whitelist, ApiIpWhitelistVO.class);
    }

    @Override
    public ApiIpBlacklistVO registerIpBlackList(ApiIpBlacklistVO apiIpBlacklistVO) {
        ApiIpBlacklist blacklist = BeanUtil.toBean(apiIpBlacklistVO, ApiIpBlacklist.class);
        LambdaQueryWrapper<ApiIpBlacklist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiIpBlacklist::getApiId, apiIpBlacklistVO.getApiId());
        ApiIpBlacklist apiIpBlacklist = blacklistMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(apiIpBlacklist)) {
            blacklistMapper.insert(blacklist);
            redisTemplate.convertAndSend(RedisKey.GATEWAY_IP_BLACKLIST_CHANNEL_KEY, JSONUtil.toJsonStr(blacklist));
        } else {
            blacklist.setId(apiIpBlacklist.getId());
            blacklistMapper.updateById(blacklist);
            if (!ObjectUtil.equals(blacklist, apiIpBlacklist)) {
                redisTemplate.convertAndSend(RedisKey.GATEWAY_IP_BLACKLIST_CHANNEL_KEY, JSONUtil.toJsonStr(blacklist));
            }
        }
        return BeanUtil.toBean(blacklist, ApiIpBlacklistVO.class);
    }

    @Override
    public ApiRateLimiterVO registerRateLimiter(ApiRateLimiterVO apiRateLimiterVO) {
        ApiRateLimiter rateLimiter = BeanUtil.toBean(apiRateLimiterVO, ApiRateLimiter.class);
        LambdaQueryWrapper<ApiRateLimiter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiRateLimiter::getApiId, apiRateLimiterVO.getApiId());
        ApiRateLimiter one = rateLimiterMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(one)) {
            rateLimiterMapper.insert(rateLimiter);
            redisTemplate.convertAndSend(RedisKey.GATEWAY_RATE_LIMITER_CHANNEL_KEY, JSONUtil.toJsonStr(rateLimiter));
        } else {
            rateLimiter.setId(one.getId());
            rateLimiterMapper.updateById(rateLimiter);
            if (!ObjectUtil.equals(rateLimiter, one)) {
                redisTemplate.convertAndSend(RedisKey.GATEWAY_RATE_LIMITER_CHANNEL_KEY, JSONUtil.toJsonStr(rateLimiter));
            }
        }
        return BeanUtil.toBean(rateLimiter, ApiRateLimiterVO.class);
    }

    @Override
    public PageVO<ApiRateLimiterVO> getRateLimiters(Integer currentPage, Integer pageSize) {
        LambdaQueryWrapper<ApiRateLimiter> queryWrapper = new LambdaQueryWrapper<>();
        Page<ApiRateLimiter> page = new Page<>(currentPage, pageSize);
        Page<ApiRateLimiter> rateLimiterPage = rateLimiterMapper.selectPage(page, queryWrapper);
        return new PageVO<>(rateLimiterPage, ApiRateLimiterVO.class);
    }

    @Override
    public ApiRateLimiterVO getRateLimiterByApiId(Long apiId) {
        LambdaQueryWrapper<ApiRateLimiter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiRateLimiter::getApiId, apiId);
        ApiRateLimiter apiRateLimiter = rateLimiterMapper.selectOne(queryWrapper);
        return BeanUtil.toBean(apiRateLimiter, ApiRateLimiterVO.class);
    }

    @Override
    public ApiIpBlacklistVO getIpBlackListByApiId(Long apiId) {
        LambdaQueryWrapper<ApiIpBlacklist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiIpBlacklist::getApiId, apiId);
        ApiIpBlacklist blacklist = blacklistMapper.selectOne(queryWrapper);
        return BeanUtil.toBean(blacklist, ApiIpBlacklistVO.class);
    }

    @Override
    public ApiIpWhitelistVO getIpWhiteListByApiId(Long apiId) {
        LambdaQueryWrapper<ApiIpWhitelist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiIpWhitelist::getApiId, apiId);
        ApiIpWhitelist whitelist = whitelistMapper.selectOne(queryWrapper);
        return BeanUtil.toBean(whitelist, ApiIpWhitelistVO.class);
    }

    @Override
    public GatewayApiVO getApiByApiId(Long apiId) {
        GatewayApi gatewayApi = gatewayApiMapper.selectById(apiId);
        return BeanUtil.toBean(gatewayApi, GatewayApiVO.class);
    }

    @Override
    public ApiInvokeLogVO recordAPIInvokeLog(ApiInvokeLogVO apiInvokeLogVO) {
        invokeLogMapper.insert(BeanUtil.toBean(apiInvokeLogVO, ApiInvokeLog.class));
        return apiInvokeLogVO;
    }
}
