import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.eardh.gateway.center.GatewayCenterApplication;
import com.eardh.gateway.center.config.GlobalConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author eardh
 * @date 2023/3/23 14:16
 */
@SpringBootTest(classes = GatewayCenterApplication.class)
@Slf4j
class ApiTest {

    @Value("${nacos.data-id}")
    private String dataId;

    @Value("${nacos.group}")
    private String group;

    @Resource
    private ConfigService configService;

    @Test
    public void testTryAcquire() throws InterruptedException, NacosException {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setRegistry("nacos://127.0.0.1:8848");
        globalConfig.setRedisAddress("127.0.0.1:6379");
        configService.publishConfig(dataId, group, JSON.toJSONString(globalConfig));
    }
}
