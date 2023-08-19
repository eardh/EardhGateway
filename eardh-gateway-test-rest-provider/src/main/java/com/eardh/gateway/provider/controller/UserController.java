package com.eardh.gateway.provider.controller;

import com.eardh.gateway.common.model.Person;
import com.eardh.gateway.common.util.JwtUtils;
import com.eardh.gateway.sdk.annotation.Api;
import com.eardh.gateway.sdk.annotation.HttpMethod;
import com.eardh.gateway.sdk.annotation.rest.RestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eardh
 * @date 2023/4/13 21:02
 */
@RestController
@RequestMapping("/user")
@RestService(api = @Api(path = "/user"))
public class UserController {

    @PostMapping("/login")
    @RestService(description = "登录", api = @Api(path = "/login", method = HttpMethod.post))
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);

        result.put("token", JwtUtils.createToken(map));
        return result;
    }

    @GetMapping("/info")
    @RestService(description = "用户信息", api = @Api(path = "/info", auth = true, method = HttpMethod.get))
    public Person userInfo(String username) {
        return new Person(username, username);
    }
}
