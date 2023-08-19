package com.eardh.gateway.sdk.registry;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.sdk.config.MicroserviceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.springframework.beans.factory.DisposableBean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author eardh
 * @date 2023/4/5 12:48
 */
@Slf4j
public class NacosRegistryService implements RegistryService, DisposableBean {

    private final String registryAddress;

    private MicroserviceProperties microserviceProperties;

    private NamingService namingService;

    private final Lock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    private ExecutorService executorService = ThreadUtil.newSingleExecutor();

    private AtomicBoolean await = new AtomicBoolean(false);

    public NacosRegistryService(MicroserviceProperties microserviceProperties, String registryAddress) {
        this.microserviceProperties = microserviceProperties;
        this.registryAddress = registryAddress;
    }

    @Override
    public void initRegistry() throws NacosException {
        namingService = NacosFactory.createNamingService(registryAddress);
    }

    @Override
    public void registerInstance() throws NacosException {
        MicroserviceProperties.Config config = microserviceProperties.getConfig();
        Instance instance = new Instance();
        instance.setEnabled(true);
        instance.setIp(NetUtils.getLocalHost());
        instance.setPort(config.getPort());
        instance.setHealthy(true);

        Map<String, String> meta = new HashMap<>();
        meta.put("serialization", config.getSerialization());
        meta.put("protocol", config.getProtocol().name());
        meta.put("version", config.getVersion());
        instance.setWeight(config.getWeight());
        instance.setMetadata(meta);
        namingService.registerInstance(microserviceProperties.getName(), microserviceProperties.getGroup(), instance);
        startAwait();
    }

    private void startAwait() {
        try {
            lock.lock();
            if (!await.get()) {
                executorService.submit(() -> {
                    while (!await.get()) {
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                        }
                    }
                    log.info("rpc服务关闭");
                });
            }
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void destroy() throws Exception {
        executorService.submit(() -> {
            try {
                lock.lock();
                await.set(true);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        });
    }
}
