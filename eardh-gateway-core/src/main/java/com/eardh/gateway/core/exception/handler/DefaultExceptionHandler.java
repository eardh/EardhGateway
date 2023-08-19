package com.eardh.gateway.core.exception.handler;

import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.util.Constant;
import com.eardh.gateway.core.util.Result;
import com.eardh.gateway.core.wapper.Response;

import java.util.Map;

import static com.eardh.gateway.core.exception.ErrorEnum.SYSTEM_EXCEPTION;

/**
 * @author eardh
 * @date 2023/3/30 16:50
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public Response handle(Exception e, HandlerContext handlerContext) {
        Response response = handlerContext.getResponse();
        if (e instanceof GateWayException) {
            Map<String, String> headers = response.getHeaders();
            headers.put(Constant.MEDIA_TYPE, Constant.APPLICATION_JSON);
            Result result = new Result(((GateWayException) e).getCode(),
                    ((GateWayException) e).getMsg(), null);
            response.setBody(result);
        } else {
            Result error = Result.error(SYSTEM_EXCEPTION.getCode(), e.getMessage());
            response.setBody(error);
        }
        return response;
    }
}
