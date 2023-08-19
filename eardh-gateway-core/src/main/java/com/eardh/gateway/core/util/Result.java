package com.eardh.gateway.core.util;

import lombok.Data;

import java.io.Serializable;


@Data
public class Result implements Serializable {

    private String code;

    private String msg;

    private Object data;

    public Result(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result ok(Object data) {
        return new Result("0000", "成功", data);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

}
