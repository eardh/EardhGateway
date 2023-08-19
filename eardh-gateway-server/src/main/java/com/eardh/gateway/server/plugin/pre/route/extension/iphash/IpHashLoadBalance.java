package com.eardh.gateway.server.plugin.pre.route.extension.iphash;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.server.plugin.pre.route.extension.LoadBalance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author eardh
 * @date 2023/4/8 21:21
 */
@Component
public class IpHashLoadBalance extends LoadBalance {

    @Override
    public Instance doSelect(Request requestWrapper, List<Instance> instanceNodes) {
        int hashCode = requestWrapper.getClientIp().hashCode();
        int index = hashCode % instanceNodes.size();
        return instanceNodes.get(index);
    }
}
