package com.eardh.gateway.common.model;

import lombok.Data;

/**
 * @author eardh
 * @date 2023/4/14 20:24
 */
@Data
public class Person {

    private String username;

    private String password;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
