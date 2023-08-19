package com.eardh.gateway.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author eardh
 * @date 2023/3/23 11:32
 */
@Schema(description = "响应实体")
@Data
public class Result<T> {

    @Schema(description = "状态码")
    private String code;

    @Schema(description = "信息")
    private String msg;

    @Schema(description = "数据")
    private T data;

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
