package com.eardh.gateway.center.controller;

import com.eardh.gateway.center.config.GlobalConfig;
import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.ResultCode;
import com.eardh.gateway.center.model.gateway.vo.GatewayServerVO;
import com.eardh.gateway.center.service.GlobalConfigService;
import com.eardh.gateway.center.service.IGatewayManageService;
import com.eardh.gateway.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 网关配置管理
 * @author eardh
 * @date 2023/3/23 11:06
 */
@RestController
@RequestMapping("/wg/admin/gateway")
@Slf4j
@Tag(name = "网关管理接口", description = "网关信息查询获取")
public class GatewayManageController {

    @Resource
    private IGatewayManageService gatewayManageService;

    @Resource
    private GlobalConfigService globalConfigService;

    /**
     * 注册网关服务节点
     * @param gatewayServerVO 需要注册的网关实体
     * @return 注册状态
     */
    @PostMapping( "/register-gateway")
    @Operation(summary = "注册网关节点")
    public Result<String> registerGatewayServer(@RequestBody GatewayServerVO gatewayServerVO) {
        try {
            log.info("注册网关服务节点 {} {} {}", gatewayServerVO.getGatewayName(), gatewayServerVO.getGatewayDescription(), gatewayServerVO.getGroupName());
            GatewayServerVO serverVO = gatewayManageService.registerGatewayServer(gatewayServerVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), serverVO.getGatewayId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/gateway-list")
    @Operation(summary = "获取网关服务列表")
    public Result<PageVO<GatewayServerVO>> getGatewayServers(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            PageVO<GatewayServerVO> pageVO = gatewayManageService.getGatewayServers(currentPage, pageSize);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), pageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/global-config")
    @Operation(summary = "获取网关全局配置")
    public Result<GlobalConfig> getGatewayServers() {
        try {
            log.info("获取网关全局配置");
            GlobalConfig globalConfig = globalConfigService.getGlobalConfig();
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), globalConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }
}
