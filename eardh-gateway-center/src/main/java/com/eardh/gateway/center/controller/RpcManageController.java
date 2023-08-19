package com.eardh.gateway.center.controller;

import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.ResultCode;
import com.eardh.gateway.center.model.aggregator.DubboRoute;
import com.eardh.gateway.center.model.aggregator.RestRoute;
import com.eardh.gateway.center.model.rpc.ApplicationInstance;
import com.eardh.gateway.center.model.rpc.vo.MicroServiceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboInterfaceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboMethodVO;
import com.eardh.gateway.center.model.rpc.vo.RestInterfaceVO;
import com.eardh.gateway.center.service.IRpcManageService;
import com.eardh.gateway.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * RPC管理，用于dubbo、rest等rpc协议的调用元数据注册
 * @author eardh
 * @date 2023/3/23 14:40
 */
@RestController
@RequestMapping("/wg/admin/rpc")
@Slf4j
@Tag(name = "RPC管理")
public class RpcManageController {

    @Resource
    private IRpcManageService rpcManageService;

    /**
     * 注册应用服务
     * @param microServiceVO 应用vo
     * @return 应用标识
     */
    @PostMapping("/register-microservice")
    @Operation(summary = "注册微服务")
    public Result<String> registerMicroservice(@RequestBody MicroServiceVO microServiceVO) {
        try {
            log.info("注册微服务 microserviceId：{}", microServiceVO.getMicroserviceName());
            MicroServiceVO application = rpcManageService.registerMicroservice(microServiceVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), application.getMicroserviceId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/microservice-list")
    @Operation(summary = "获取微服务列表")
    public Result<PageVO<MicroServiceVO>> getMicroservices(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            PageVO<MicroServiceVO> pageVO = rpcManageService.getMicroservices(currentPage, pageSize);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), pageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    /**
     * 注册Dubbo服务接口类信息
     * @param dubboInterfaceVO 接口vo
     * @return 标识
     */
    @PostMapping("/register-dubbo_interface")
    @Operation(summary = "注册Dubbo服务接口类信息")
    public Result<String> registerDubboInterface(@RequestBody DubboInterfaceVO dubboInterfaceVO) {
        try {
            log.info("注册Dubbo服务接口类信息 microserviceId：{} interfaceId：{}", dubboInterfaceVO.getMicroserviceId(), dubboInterfaceVO.getInterfaceId());
            DubboInterfaceVO vo = rpcManageService.registerDubboInterface(dubboInterfaceVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), vo.getInterfaceId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    /**
     * 注册Dubbo服务方法信息
     * @param dubboMethodVO 方法vo
     * @return 标识
     */
    @PostMapping("/register-dubbo_method")
    @Operation(summary = "注册Dubbo服务方法信息")
    public Result<String> registerDubboMethod(@RequestBody DubboMethodVO dubboMethodVO) {
        try {
            log.info("注册Dubbo服务方法信息 microserviceId：{} interfaceId：{} methodId：{}",
                    dubboMethodVO.getMicroserviceId(), dubboMethodVO.getInterfaceId(), dubboMethodVO.getMethodId());
            DubboMethodVO method = rpcManageService.registerDubboMethod(dubboMethodVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), method.getMethodId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/dubbo-route-list")
    @Operation(summary = "获取Dubbo路由列表")
    public Result<PageVO<DubboRoute>> getDubboRoutes(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "requireApiId", defaultValue = "false") Boolean requireApiId) {
        try {
            log.info("获取dubbo路由列表");
            PageVO<DubboRoute> pageVO = rpcManageService.getDubboRoutes(currentPage, pageSize, requireApiId);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), pageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @PostMapping("/register-rest-route")
    @Operation(summary = "注册Rest路由")
    public Result<String> registerRestRoute(@RequestBody RestInterfaceVO restInterfaceVO) {
        try {
            log.info("注册Rest路由 {} {}", restInterfaceVO.getRestUrl(), restInterfaceVO.getRestMethod());
            RestInterfaceVO route = rpcManageService.registerRestRoute(restInterfaceVO);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), route.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/rest-route-list")
    @Operation(summary = "获取Rest路由列表")
    public Result<PageVO<RestRoute>> getRestRoutes(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "requireApiId", defaultValue = "false") Boolean requireApiId) {
        try {
            log.info("获取网关rest路由列表");
            PageVO<RestRoute> pageVO = rpcManageService.getRestRoutes(currentPage, pageSize, requireApiId);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), pageVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @PostMapping("/update-rest-route")
    @Operation(summary = "更新Rest路由")
    public Result<String> updateRestRoute(@RequestBody RestRoute restRoute) {
        try {
            log.info("更新Rest路由 {} {}", restRoute.getApiId(), restRoute.getRestUrl());
            rpcManageService.updateRestRoute(restRoute);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @PostMapping("/update-dubbo-route")
    @Operation(summary = "更新Dubbo路由")
    public Result<String> updateDubboRoute(@RequestBody DubboRoute dubboRoute) {
        try {
            log.info("更新Dubbo路由 {} {}", dubboRoute.getApiId(), dubboRoute.getMicroserviceName());
            rpcManageService.updateDubboRoute(dubboRoute);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/application-instances")
    @Operation(summary = "查询应用实例")
    public Result<List<ApplicationInstance>> getApplicationInstances(@RequestParam(value = "microserviceId") String microserviceId) {
        try {
            log.info("查询应用实例 {}", microserviceId);
            List<ApplicationInstance> applicationInstances = rpcManageService.getApplicationInstances(microserviceId);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), applicationInstances);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/gateway-instances")
    @Operation(summary = "查询网关实例")
    public Result<List<ApplicationInstance>> getGatewayInstances(@RequestParam(value = "gatewayId") String gatewayId) {
        try {
            log.info("查询网关实例 {}", gatewayId);
            List<ApplicationInstance> applicationInstances = rpcManageService.getGatewayInstances(gatewayId);
            return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getInfo(), applicationInstances);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(ResultCode.ERROR.getCode(), e.getMessage(), null);
        }
    }


}

