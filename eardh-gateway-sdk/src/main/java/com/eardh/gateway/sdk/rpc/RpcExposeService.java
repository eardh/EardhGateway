package com.eardh.gateway.sdk.rpc;

import java.util.List;

/**
 * @author eardh
 * @date 2023/4/5 13:26
 */
public interface RpcExposeService {

    void exposeServices(List<Object> beans);

}
