package com.eardh.gateway.sdk.rpc;

import cn.hutool.core.thread.ThreadUtil;
import com.eardh.gateway.sdk.annotation.dubbo.DubboService;
import com.eardh.gateway.sdk.config.MicroserviceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.context.ModuleConfigManager;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.FrameworkModel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author eardh
 * @date 2023/4/5 13:30
 */
@Slf4j
public class DubboRpcExposeService implements RpcExposeService, InitializingBean, DisposableBean {

    private MicroserviceProperties microserviceProperties;

    private ApplicationModel applicationModel;

    private final Lock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    private ExecutorService executorService = ThreadUtil.newSingleExecutor();

    private AtomicBoolean await = new AtomicBoolean(false);

    public DubboRpcExposeService(MicroserviceProperties serviceProperties) {
        this.microserviceProperties = serviceProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ApplicationModel applicationModel = FrameworkModel.defaultModel().newApplication();

        ApplicationConfig applicationConfig = new ApplicationConfig(microserviceProperties.getName());
        applicationConfig.setQosEnable(false);
        applicationConfig.setRegisterMode("instance");

        ProtocolConfig protocolConfig = new ProtocolConfig(microserviceProperties.getConfig().getProtocol().name(), microserviceProperties.getConfig().getPort());
        protocolConfig.setSerialization(microserviceProperties.getConfig().getSerialization());

        applicationModel.getApplicationConfigManager().setApplication(applicationConfig);
        applicationModel.getApplicationConfigManager().addProtocol(protocolConfig);

        this.applicationModel = applicationModel;
    }

    @Override
    public void exposeServices(List<Object> beans) {
        ModuleConfigManager configManager = applicationModel.getDefaultModule().getConfigManager();
        for (Object bean : beans) {
            DubboService apiService = bean.getClass().getAnnotation(DubboService.class);
            String group = apiService.group();
            Class<?> aClass = apiService.$interface();
            if (aClass.getName().equals(DubboService.class.getName())) {
                ServiceConfig<Object> service = new ServiceConfig<>();
                service.setInterface(bean.getClass().getInterfaces()[0]);
                service.setRef(bean);
                service.setGroup(group);
                configManager.addService(service);
            } else {
                ServiceConfig<Object> service = new ServiceConfig<>();
                service.setInterface(aClass);
                service.setRef(bean);
                service.setGroup(group);
                configManager.addService(service);
            }
        }
        dubboStartAwait();
    }

    private void dubboStartAwait() {
        try {
            lock.lock();
            if (!await.get()) {
                executorService.submit(() -> {
                    applicationModel.getDeployer().start();
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
