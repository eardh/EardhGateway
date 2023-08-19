package com.eardh.gateway.core.server.netty;

import com.eardh.gateway.core.server.HttpServer;
import com.eardh.gateway.core.server.netty.handler.HttpServerInboundHandler;
import com.eardh.gateway.core.GatewayExecutor;
import com.eardh.gateway.core.server.netty.config.NettyConfig;
import com.eardh.gateway.core.server.netty.constant.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * 使用Netty作为http服务的容器
 *
 * @author eardh
 */
@Slf4j
public class NettyHttpServer implements HttpServer {

    /**
     * netty 配置
     */
    private NettyConfig nettyConfig;

    /**
     * 网关执行器
     */
    private GatewayExecutor gatewayExecutor;

    /**
     * bossGroup
     */
    private EventLoopGroup bossGroup;

    /**
     * workerGroup
     */
    private EventLoopGroup workerGroup;

    public NettyHttpServer(NettyConfig nettyConfig, GatewayExecutor gatewayExecutor) {
        this.nettyConfig = Objects.requireNonNull(nettyConfig, "nettyConfig must not be null !");
        this.gatewayExecutor = Objects.requireNonNull(gatewayExecutor, "gatewayExecutor must not be null !");
        this.bossGroup = new NioEventLoopGroup(this.nettyConfig.getBossGroupNThread());
        this.workerGroup = new NioEventLoopGroup(this.nettyConfig.getWorkerGroupNThread());
    }

    @Override
    public void start() throws Exception {
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(nettyConfig.getPort()))
                    .option(ChannelOption.SO_BACKLOG, nettyConfig.getSo_backLog())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(NettyConstant.HTTP_SERVER_CODEC,
                                            new HttpServerCodec())
                                    .addLast(NettyConstant.AGGREGATOR,
                                            new HttpObjectAggregator(nettyConfig.getAggregator() * 1024))
                                    .addLast(NettyConstant.HTTP_SERVER_HANDLER,
                                            new HttpServerInboundHandler(gatewayExecutor));
                        }
                    });
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = server.bind(nettyConfig.getPort()).sync();
            f.addListener((ChannelFutureListener) future -> log.info("网关Netty服务启动完成"));
        } catch (Exception e) {
            log.info("网关Netty服务启动异常");
            stop();
        }
    }

    @Override
    public void stop() throws Exception {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("网关Netty服务关闭成功");
    }
}
