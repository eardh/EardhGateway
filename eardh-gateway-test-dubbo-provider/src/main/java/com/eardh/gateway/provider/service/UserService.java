package com.eardh.gateway.provider.service;


import com.eardh.gateway.common.model.Person;

import java.util.Map;

/**
 * @author eardh
 * @date 2023/3/15 15:35
 */
public interface UserService {

    Map<String, Object> login(String username, String password);

    Person userInfo(String username);
}
