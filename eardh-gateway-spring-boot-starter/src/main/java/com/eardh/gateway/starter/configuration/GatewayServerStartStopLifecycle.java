package com.eardh.gateway.starter.configuration;

import com.eardh.gateway.core.GatewayServer;
import org.springframework.context.SmartLifecycle;

/**
 * @author eardh
 * @date 2023/3/31 9:55
 */
public class GatewayServerStartStopLifecycle implements SmartLifecycle {

    private final GatewayServer gatewayServer;

    private boolean running;

    public GatewayServerStartStopLifecycle(GatewayServer gatewayServer) {
        this.gatewayServer = gatewayServer;
        running = false;
    }

    @Override
    public void start() {
        if (!running) {
            try {
                gatewayServer.start();
                running = true;
            } catch (Exception e) {
                e.printStackTrace();
                stop();
            }
        }
    }

    @Override
    public void stop() {
        if (running) {
            try {
                gatewayServer.stop();
                running = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
