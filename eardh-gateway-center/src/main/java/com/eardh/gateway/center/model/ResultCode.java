package com.eardh.gateway.center.model;

/**
 * @author eardh
 * @date 2023/3/23 11:34
 */
public enum ResultCode {

    SUCCESS("0000", "成功"),
    ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    INDEX_DUP("0003", "主键冲突"),
    NO_UPDATE("0004", "SQL操作无更新");

    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return msg;
    }

}
