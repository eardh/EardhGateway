package com.eardh.gateway.core.exception;

import lombok.Getter;

/**
 * gateWay 网关异常
 *
 * @author eardh
 */
public class GateWayException extends RuntimeException {

    @Getter
    private String code;

    @Getter
    private String msg;

    public GateWayException(String code, String msg) {
        super(code + ":" + msg);
        this.code = code;
        this.msg = msg;
    }

    public GateWayException(ErrorEnum errorEnum) {
        super(errorEnum.getCode() + ":" + errorEnum.getMsg());
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }
}
