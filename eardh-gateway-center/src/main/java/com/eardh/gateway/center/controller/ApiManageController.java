package com.eardh.gateway.center.controller;

import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.ResultCode;
import com.eardh.gateway.center.model.api.vo.*;
import com.eardh.gateway.center.service.impl.ApiManageService;
import com.eardh.gateway.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author eardh
 * @date 2023/4/3 12:16
 */
@RestController
@RequestMapping("/wg/admin/api")
@Slf4j
@Tag(name = "API管理接口", description = "网关API信息")
public class ApiManageController {

    @Resource
    private ApiManageService apiManageService;

    @PostMapping("/register-api")
    @Operation(summary = "注册网关API")
    public Result<String> registerApi(@RequestBody GatewayApiVO gatewayApiVO) {
        try {
            log.info("注册网关API {} {}", gatewayApiVO.getApiPath(), gatewayApiVO.getApiMethod());
            GatewayApiVO gatewayApiVO1 = apiManageService.registerApi(gatewayApiVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), gatewayApiVO1.getApiId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/api-list")
    @Operation(summary = "获取API列表")
    public Result<PageVO<GatewayApiVO>> getApis(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            log.info("获取网关API列表");
            PageVO<GatewayApiVO> pageVO = apiManageService.getApis(currentPage, pageSize);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), pageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/api-by_apiId")
    @Operation(summary = "根据ID查询API")
    public Result<GatewayApiVO> getApiByApiId(@RequestParam("apiId") Long apiId) {
        try {
            log.info("根据ID查询API");
            GatewayApiVO gatewayApiVO = apiManageService.getApiByApiId(apiId);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), gatewayApiVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/ip_white-list")
    @Operation(summary = "获取API白名单")
    public Result<PageVO<ApiIpWhitelistVO>> getIpWhiteList(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            log.info("获取API白名单");
            PageVO<ApiIpWhitelistVO> pageVO = apiManageService.getIpWhiteList(currentPage, pageSize);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), pageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @PostMapping("/register-ip_whitelist")
    @Operation(summary = "注册API白名单")
    public Result<String> registerIpWhiteList(@RequestBody ApiIpWhitelistVO apiIpWhitelistVO) {
        try {
            log.info("注册API白名单 {}", apiIpWhitelistVO.getApiId());
            ApiIpWhitelistVO apiIpWhitelistVO1 = apiManageService.registerIpWhiteList(apiIpWhitelistVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/ip_whitelist-by-apiId")
    @Operation(summary = "通过ApiId获取白名单")
    public Result<ApiIpWhitelistVO> getIpWhiteListByApiId(@RequestParam(value = "apiId") Long apiId) {
        try {
            log.info("通过ApiId获取白名单");
            ApiIpWhitelistVO whitelistVO = apiManageService.getIpWhiteListByApiId(apiId);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), whitelistVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }


    @GetMapping("/ip_black-list")
    @Operation(summary = "获取API黑名单")
    public Result<PageVO<ApiIpBlacklistVO>> getIpBlackList(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            log.info("获取API黑名单");
            PageVO<ApiIpBlacklistVO> pageVO = apiManageService.getIpBlackList(currentPage, pageSize);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), pageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @PostMapping("/register-ip_blacklist")
    @Operation(summary = "注册API黑名单")
    public Result<String> registerIpBlackList(@RequestBody ApiIpBlacklistVO apiIpBlacklistVO) {
        try {
            log.info("注册API黑名单 {}", apiIpBlacklistVO.getApiId());
            ApiIpBlacklistVO apiIpBlacklistVO1 = apiManageService.registerIpBlackList(apiIpBlacklistVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/ip_blacklist-by-apiId")
    @Operation(summary = "通过ApiId获取黑名单")
    public Result<ApiIpBlacklistVO> getIpBlackListByApiId(@RequestParam(value = "apiId") Long apiId) {
        try {
            log.info("通过ApiId获取黑名单");
            ApiIpBlacklistVO apiIpBlacklistVO = apiManageService.getIpBlackListByApiId(apiId);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), apiIpBlacklistVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @PostMapping("/register-rate_limiter")
    @Operation(summary = "注册API限流规则")
    public Result<String> registerRateLimiter(@RequestBody ApiRateLimiterVO apiRateLimiterVO) {
        try {
            log.info("注册API限流规则 {}", apiRateLimiterVO.getApiId());
            ApiRateLimiterVO apiRateLimiterVO1 = apiManageService.registerRateLimiter(apiRateLimiterVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/rate_limiter-list")
    @Operation(summary = "获取API限流规则")
    public Result<PageVO<ApiRateLimiterVO>> getRateLimiters(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            log.info("获取API限流规则");
            PageVO<ApiRateLimiterVO> pageVO = apiManageService.getRateLimiters(currentPage, pageSize);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), pageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/rate_limiter-by-apiId")
    @Operation(summary = "通过ApiId获取限流规则")
    public Result<ApiRateLimiterVO> getRateLimiterByApiId(@RequestParam(value = "apiId") Long apiId) {
        try {
            log.info("通过ApiId获取限流规则");
            ApiRateLimiterVO apiRateLimiterVO = apiManageService.getRateLimiterByApiId(apiId);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), apiRateLimiterVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @PostMapping("/api-invoke-log")
    @Operation(summary = "API调用日志")
    public Result<String> recordAPIInvokeLog(@RequestBody ApiInvokeLogVO apiInvokeLogVO) {
        try {
            log.info("API调用日志 {}", apiInvokeLogVO.getApiId());
            ApiInvokeLogVO apiInvokeLogVO1 = apiManageService.recordAPIInvokeLog(apiInvokeLogVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }
}
