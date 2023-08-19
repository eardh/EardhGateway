package com.eardh.gateway.core.server.netty.converter;

import com.alibaba.fastjson2.JSON;
import com.eardh.gateway.core.wapper.Response;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.UnsupportedEncodingException;

/**
 * 负责将通用的请求转化为Netty的converter
 *
 * @author eardh
 */
public class NettyConverter {

    /**
     * 将 responseWrapper 转化为FullHttpResponse
     *
     * @param responseWrapper {@link Response}
     * @return {@link FullHttpResponse}
     * @throws UnsupportedEncodingException exception
     */
    public static FullHttpResponse convertToNettyResponse(Response responseWrapper) throws UnsupportedEncodingException {
        HttpResponseStatus status = HttpResponseStatus.valueOf(responseWrapper.getStatusCode());
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer(JSON.toJSONBytes(responseWrapper.getBody())));
        responseWrapper.getHeaders().forEach((k, v) -> response.headers().add(k, v));
        return response;
    }
}
