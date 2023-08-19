package com.eardh.gateway.server.plugin.pre.route.extension.weight;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.server.plugin.pre.route.extension.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @author eardh
 * @date 2023/4/8 21:21
 */
public class WeightLoadBalance extends LoadBalance {

    @Override
    public Instance doSelect(Request requestWrapper, List<Instance> instances) {
        instances.sort((n1, n2) -> {
            if (n1.getWeight() < n2.getWeight()) {
                return 1;
            } else if (n1.getWeight() > n2.getWeight()) {
                return -1;
            }
            return 0;
        });
        double sum = instances.stream().mapToDouble(Instance::getWeight).sum();
        double random = new Random().nextDouble();
        for (Instance instanceNode : instances) {
            double per = instanceNode.getWeight() / sum;
            if (per >= random) {
                return instanceNode;
            }
        }
        return null;
    }
}
