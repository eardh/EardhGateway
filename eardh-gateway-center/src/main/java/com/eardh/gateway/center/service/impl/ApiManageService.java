package com.eardh.gateway.center.service.impl;

import com.eardh.gateway.center.model.api.vo.*;
import com.eardh.gateway.center.repository.impl.ApiManageRepository;
import com.eardh.gateway.center.service.IApiManageService;
import com.eardh.gateway.center.model.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author eardh
 * @date 2023/4/3 12:22
 */
@Service
public class ApiManageService implements IApiManageService {

    @Resource
    private ApiManageRepository apiRepository;

    @Override
    public GatewayApiVO registerApi(GatewayApiVO gatewayApiVO) {
        return apiRepository.registerApi(gatewayApiVO);
    }

    @Override
    public PageVO<GatewayApiVO> getApis(Integer currentPage, Integer pageSize) {
        return apiRepository.getApis(currentPage, pageSize);
    }

    @Override
    public PageVO<ApiIpWhitelistVO> getIpWhiteList(Integer currentPage, Integer pageSize) {
        return apiRepository.getIpWhiteList(currentPage, pageSize);
    }

    @Override
    public PageVO<ApiIpBlacklistVO> getIpBlackList(Integer currentPage, Integer pageSize) {
        return apiRepository.getIpBlackList(currentPage, pageSize);
    }

    @Override
    public ApiIpWhitelistVO registerIpWhiteList(ApiIpWhitelistVO apiIpWhitelistVO) {
        return apiRepository.registerIpWhiteList(apiIpWhitelistVO);
    }

    @Override
    public ApiIpBlacklistVO registerIpBlackList(ApiIpBlacklistVO apiIpBlacklistVO) {
        return apiRepository.registerIpBlackList(apiIpBlacklistVO);
    }

    @Override
    public ApiRateLimiterVO registerRateLimiter(ApiRateLimiterVO apiRateLimiterVO) {
        return apiRepository.registerRateLimiter(apiRateLimiterVO);
    }

    @Override
    public PageVO<ApiRateLimiterVO> getRateLimiters(Integer currentPage, Integer pageSize) {
        return apiRepository.getRateLimiters(currentPage, pageSize);
    }

    @Override
    public ApiRateLimiterVO getRateLimiterByApiId(Long apiId) {
        return apiRepository.getRateLimiterByApiId(apiId);
    }

    @Override
    public ApiIpBlacklistVO getIpBlackListByApiId(Long apiId) {
        return apiRepository.getIpBlackListByApiId(apiId);
    }

    @Override
    public ApiIpWhitelistVO getIpWhiteListByApiId(Long apiId) {
        return apiRepository.getIpWhiteListByApiId(apiId);
    }

    @Override
    public GatewayApiVO getApiByApiId(Long apiId) {
        return apiRepository.getApiByApiId(apiId);
    }

    @Override
    public ApiInvokeLogVO recordAPIInvokeLog(ApiInvokeLogVO apiInvokeLogVO) {
        return apiRepository.recordAPIInvokeLog(apiInvokeLogVO);
    }
}
