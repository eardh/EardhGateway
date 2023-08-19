import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.provider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.apache.dubbo.common.constants.CommonConstants.GENERIC_SERIALIZATION_DEFAULT;

/**
 * @author eardh
 * @date 2023/3/9 14:08
 */
@Slf4j
public class ClientTest {

    @Test
    void t1() throws ClassNotFoundException, IOException, NacosException {

        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();

            ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
            reference.setInterface(UserService.class);
//        reference.setUrl("dubbo://10.157.47.60:50051");
            reference.setGeneric(GENERIC_SERIALIZATION_DEFAULT);
            reference.setGroup("test-user");

            ApplicationConfig applicationConfig = new ApplicationConfig("eardh-gateway-test-provider");
            applicationConfig.setQosEnable(false);

            RegistryConfig registryConfig = new RegistryConfig("nacos://127.0.0.1:8848");
            registryConfig.setGroup("appTest1");


            DubboBootstrap.getInstance()
                    .application(applicationConfig)
                    .registry(registryConfig)
                    .protocol(new ProtocolConfig("tri"))
                    .reference(reference)
                    .start();

            GenericService genericService = reference.get();
            Object o = genericService.$invoke("login",
                    new String[]{String.class.getName(), String.class.getName()},
                    new Object[]{"dahuang", "123456"});

            log.info("耗时 {}", System.currentTimeMillis() - start);

            System.out.println("Receive result ======> " + o);
            System.in.read();
        }


    }

    @Test
    void t3() throws NacosException, IOException {
        NamingService namingService = NacosFactory.createNamingService("127.0.0.1:8848");
        List<Instance> allInstances = namingService.getAllInstances("eardh-gateway-test-provider", "appTest1");
        for (Instance allInstance : allInstances) {
            System.out.println("dahuang " + allInstance.toString());
        }
        System.in.read();
    }

    @Test
    void t4() throws IOException, InterruptedException {
        System.out.println(String.class.equals(String.class));
    }
}