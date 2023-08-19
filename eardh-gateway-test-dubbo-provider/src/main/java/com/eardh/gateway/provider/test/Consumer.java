package com.eardh.gateway.provider.test;

import com.eardh.gateway.provider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.FrameworkModel;
import org.apache.dubbo.rpc.service.GenericService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.apache.dubbo.common.constants.CommonConstants.GENERIC_SERIALIZATION_DEFAULT;

/**
 * @author eardh
 * @date 2023/4/8 9:54
 */
@Slf4j
public class Consumer {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        ApplicationModel applicationModel = FrameworkModel.defaultModel().newApplication();
        ApplicationConfig applicationConfig = new ApplicationConfig("consumer");
        applicationConfig.setQosEnable(false);

        applicationModel.getApplicationConfigManager().setApplication(applicationConfig);
        applicationModel.getDeployer().start();

        long start = System.currentTimeMillis();

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface(UserService.class);
        reference.setUrl("tri://10.157.47.60:50051");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("prefer.serialization", "fastjson2");
        parameters.put("serialization", "fastjson2");
        reference.setParameters(parameters);

        reference.setGeneric(GENERIC_SERIALIZATION_DEFAULT);
        reference.setGroup("test-user");

        reference.setApplication(applicationConfig);

        GenericService genericService = reference.get();

        Object o = genericService.$invoke("login",
                new String[]{String.class.getName(), String.class.getName()},
                new Object[]{"dahuang", "123456"});

        log.info("耗时 {}", System.currentTimeMillis() - start);

        System.out.println("Receive result ======> " + o);
        System.in.read();
    }
}
