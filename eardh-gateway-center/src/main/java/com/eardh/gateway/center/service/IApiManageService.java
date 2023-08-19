package com.eardh.gateway.center.service;


import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.api.vo.*;

/**
 * @author eardh
 * @date 2023/4/3 12:19
 */
public interface IApiManageService {

    GatewayApiVO registerApi(GatewayApiVO gatewayApiVO);

    PageVO<GatewayApiVO> getApis(Integer currentPage, Integer pageSize);

    PageVO<ApiIpWhitelistVO> getIpWhiteList(Integer currentPage, Integer pageSize);

    PageVO<ApiIpBlacklistVO> getIpBlackList(Integer currentPage, Integer pageSize);

    ApiIpWhitelistVO registerIpWhiteList(ApiIpWhitelistVO apiIpWhitelistVO);

    ApiIpBlacklistVO registerIpBlackList(ApiIpBlacklistVO apiIpBlacklistVO);

    ApiRateLimiterVO registerRateLimiter(ApiRateLimiterVO apiRateLimiterVO);

    PageVO<ApiRateLimiterVO> getRateLimiters(Integer currentPage, Integer pageSize);

    ApiRateLimiterVO getRateLimiterByApiId(Long apiId);

    ApiIpBlacklistVO getIpBlackListByApiId(Long apiId);

    ApiIpWhitelistVO getIpWhiteListByApiId(Long apiId);

    GatewayApiVO getApiByApiId(Long apiId);

    ApiInvokeLogVO recordAPIInvokeLog(ApiInvokeLogVO apiInvokeLogVO);
}
