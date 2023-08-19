package com.eardh.gateway.core.server.netty.handler;

import com.eardh.gateway.core.GatewayExecutor;
import com.eardh.gateway.core.server.netty.converter.NettyConverter;
import com.eardh.gateway.core.server.netty.wapper.NettyRequestWrapper;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.core.wapper.Response;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于处理Http请求
 *
 * @author eardh
 */
@Slf4j
public class HttpServerInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * 网关执行器
     */
    private GatewayExecutor gatewayExecutor;

    public HttpServerInboundHandler(GatewayExecutor gatewayExecutor) {
        this.gatewayExecutor = gatewayExecutor;
    }

    /**
     * 用于处理Http请求
     *
     * @param cht     http请求上下文
     * @param request 请求
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext cht, FullHttpRequest request) throws Exception {
        boolean keepAlive = HttpUtil.isKeepAlive(request);

        Request requestWrapper = new NettyRequestWrapper(cht, request).buildRequestWrapper();
        log.info("full httpRequest:{}", requestWrapper);

        long start = System.currentTimeMillis();
        Response responseWrapper = gatewayExecutor.execute(requestWrapper);
        long end = System.currentTimeMillis();
        log.info("网关组件执行总耗时：{} ms", end - start);

        FullHttpResponse response = NettyConverter.convertToNettyResponse(responseWrapper);
        log.info("full responseWrapper :{}", responseWrapper);
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        }
        ChannelFuture future = cht.writeAndFlush(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (null != cause) {
            log.error(cause.getMessage(), cause);
        }
        if (null != ctx) {
            ctx.close();
        }
    }
}
