package com.eardh.gateway.core.server.netty.wapper;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.eardh.gateway.core.util.Constant;
import com.eardh.gateway.core.wapper.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 基于Netty的实现类，构造通用的request
 *
 * @author eardh
 */
public class NettyRequestWrapper extends Request {

    /**
     * 用户获取客户端ip的请求头
     */
    private static final String HTTPHEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String HTTPHEADER_X_REAL_IP = "X-Real-IP";

    /**
     * 未知ip地址头
     */
    private static final String HTTPIP_UNKNOWN = "unknown";

    private FullHttpRequest request;
    private ChannelHandlerContext cht;

    public NettyRequestWrapper(ChannelHandlerContext cht, FullHttpRequest request) {
        this.cht = cht;
        this.request = request;
    }

    @Override
    public Request buildRequestWrapper() throws IOException {
        this.uri = buildUri(request.uri());
        this.requestId = String.format("%s-%s", uri, UUID.randomUUID().toString().replaceAll("-", ""));
        this.httpMethod = request.method().name().toLowerCase();

        HttpHeaders headers = request.headers();

        buildHeaders(headers, true);

        this.mediaType = buildMediaType(headers.get(Constant.MEDIA_TYPE));
        buildParams(request.uri());

        // 对 post 请求处理，转化为键值对
        if (HttpMethod.POST.name().toLowerCase().equals(this.httpMethod)) {
            this.postBody = buildPostBody();
        }

        buildClientIp();
        return this;
    }


    /**
     * 构造请求uri，去除原始uri请求参数等
     * @param uri 原始 uri
     * @return 处理后的 uri
     */
    private String buildUri(String uri) {
        int index = uri.indexOf('?');
        return index > 0 ? uri.substring(0, index) : uri;
    }

    private String buildMediaType(String contentType) {
        if (contentType == null) {
            return null;
        }
        int index = contentType.indexOf(';');
        return index > 0 ? contentType.substring(0, index) : contentType;
    }

    /**
     * 构建 http get 请求的参数
     */
    private void buildParams(String uri) {
        Map<String, Object> requestParams = new HashMap<>(16);
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> params = decoder.parameters();
        params.forEach((k, v) -> requestParams.put(k, v.get(0)));
        this.params = requestParams;
    }

    /**
     * 构建 http post 请求的参数
     */
    private Map<String, Object> buildPostBody() throws IOException {
        Map<String, Object> body = new HashMap<>();
        if (this.mediaType != null) {
            if (this.mediaType.equalsIgnoreCase(Constant.APPLICATION_JSON)) {
                if (request.content().isReadable()) {
                    body = JSON.parseObject(request.content().toString(StandardCharsets.UTF_8), new TypeReference<Map<String, Object>>(){});
                }
            } else if (this.mediaType.equalsIgnoreCase(Constant.MULTIPART_FORM_DATA)) {
                HttpPostRequestDecoder httpPostRequestDecoder = new HttpPostRequestDecoder(request);
                for (InterfaceHttpData bodyHttpData : httpPostRequestDecoder.getBodyHttpDatas()) {
                    if (bodyHttpData.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        body.put(bodyHttpData.getName(), ((Attribute) bodyHttpData).getValue());
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
            }
        }
        return body;
    }

    /**
     * 构建headers
     *
     * @param headers headers
     * @param toLower 是否转化为小写
     */
    private void buildHeaders(HttpHeaders headers, boolean toLower) {
        Map<String, String> stringHeaders = new HashMap<>(16);
        if (toLower) {
            headers.forEach(entry -> stringHeaders.put(entry.getKey().toLowerCase(), entry.getValue()));
            this.headers = stringHeaders;
        } else {
            headers.forEach(entry -> stringHeaders.put(entry.getKey(), entry.getValue()));
            this.headers = stringHeaders;
        }
        this.headers.put("requestId", requestId);
    }

    /**
     * 生成客户端ip地址
     */
    private void buildClientIp() {
        String clientIp = request.headers().get(HTTPHEADER_X_FORWARDED_FOR);
        if (clientIp == null || clientIp.length() == 0 || HTTPIP_UNKNOWN.equalsIgnoreCase(clientIp)) {
            clientIp = request.headers().get(HTTPHEADER_X_REAL_IP);
        }
        if (clientIp == null || clientIp.length() == 0 || HTTPIP_UNKNOWN.equalsIgnoreCase(clientIp)) {
            InetSocketAddress insocket = (InetSocketAddress) cht.channel().remoteAddress();
            clientIp = insocket.getAddress().getHostAddress();
        }
        this.clientIp = clientIp;
    }
}
