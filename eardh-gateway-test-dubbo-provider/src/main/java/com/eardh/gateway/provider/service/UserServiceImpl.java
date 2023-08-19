package com.eardh.gateway.provider.service;

import com.eardh.gateway.common.model.Person;
import com.eardh.gateway.common.util.JwtUtils;
import com.eardh.gateway.sdk.annotation.Api;
import com.eardh.gateway.sdk.annotation.HttpMethod;
import com.eardh.gateway.sdk.annotation.dubbo.DubboService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eardh
 * @date 2023/3/15 15:36
 */
@Component
@DubboService(description = "用户服务", group = "test-user", api = @Api(path = "/user"))
public class UserServiceImpl implements UserService {

    @Override
    @DubboService(description = "用户登录", api = @Api(path = "/login", method = HttpMethod.post))
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);

        result.put("token", JwtUtils.createToken(map));
        return result;
    }

    @Override
    @DubboService(description = "用户信息", api = @Api(path = "/info", auth = true, method = HttpMethod.get))
    public Person userInfo(String username) {
        return new Person(username, username);
    }
}
