package com.eardh.gateway.core.exception;

/**
 * @author eardh
 * @date 2023/4/3 17:13
 */
public enum ErrorEnum {

    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION("1001","系统异常"),

    /**
     * api白名单
     */
    WHITE_LIST_HANDLER("1002","您没有权限访问该api"),

    /**
     * 请求被限流
     */
    RATE_LIMIT_HANDLER("1003","请求频繁，请稍后再试"),

    /**
     * 签名失败
     */
    SING_HANDLER("1004","签名失败，请重新登陆"),

    /**
     * 请求超时
     */
    REQUEST_TIME_OUT("1005","请求超时"),

    /**
     * 请求异常
     */
    REQUEST_ERROR("1006","请求异常"),

    /**
     * api不可用
     */
    API_NOT_AVALIABLE("1007","该api不可用"),

    /**
     * 不存在的api
     */
    WRONG_API_EXCEPTION("1008","不存在的api"),

    /**
     * token无效
     */
    TOKEN_HANDLER("1009","token无效，请重新登陆"),

    API_NOT_PROVIDER("1010", "api无服务提供者映射"),

    /**
     * userId不允许为空
     */
    USER_ID_NULL("1011","userId不允许为空！"),

    RPC_INVOKE_ERROR("1012","rpc调用错误！"),

    IP_BLACKLIST_ERROR("1013","请求IP被黑名单拦截！"),

    IP_WHITELIST_ERROR("1014","请求IP不在白名单范围！"),

    RATE_LIMITER_ERROR("1015","请求已被限流！");

    private final String code;

    private final String msg;

    ErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
