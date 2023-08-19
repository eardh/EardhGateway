package com.eardh.gateway.server.invocation.dubbo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.core.util.PrimaryType;
import com.eardh.gateway.server.storage.route.dubbo.DubboRoute;
import lombok.Data;

import java.util.Map;

/**
 * @author eardh
 * @date 2023/4/8 20:55
 */
@Data
public class DubboCallStatement {

    /**
     * 调用的服务主机ip
     */
    private String ip;

    /**
     * 调用的服务主机端口
     */
    private Integer port;

    /**
     * 目的主机所使用的rpc协议类型
     */
    private String protocol;

    /**
     *  目的主机所使用的对象序列化方法
     */
    private String serialization;

    /**
     *  调用的服务接口名称
     */
    private String interfaceName;

    /**
     * 调用的服务接口所属组
     */
    private String interfaceGroup;

    /**
     * 调用的服务方法名
     */
    private String methodName;

    /**
     * 方法参数类型列表
     */
    private String[] argsType;

    /**
     *  方法参数值列表
     */
    private Object[] argsValue;

    /**
     * 方法返回值类型
     */
    private String resultType;

    public DubboCallStatement(Instance instance) {
        this.ip = instance.getIp();
        this.port = instance.getPort();
        this.protocol = instance.getMetadata().getOrDefault("protocol", "tri");
        this.serialization = instance.getMetadata().getOrDefault("serialization", "fastjson2");
    }

    public <T> void parseRequest(Map<String, T> params, DubboRoute dubboRoute) throws ClassNotFoundException {
        interfaceName = dubboRoute.getInterfaceName();
        interfaceGroup = dubboRoute.getInterfaceGroup();
        methodName = dubboRoute.getMethodName();
        resultType = dubboRoute.getResultType();
        argsType = dubboRoute.getParameterTypes();

        Object[] argValue = new Object[dubboRoute.getParameterTypes().length];
        String[] parameterNames = dubboRoute.getParameterNames();
        for (int i = 0; i < argValue.length; i++) {
            T value;
            if (PrimaryType.isPrimary(argsType[i])) {
                value = params.get(parameterNames[i]);
            } else {
                value = BeanUtil.mapToBean(params, (Class<T>) Class.forName(parameterNames[i]), true, CopyOptions.create());
            }
            argValue[i] = value;
        }
        argsValue = argValue;
    }
}
