package com.eardh.gateway.core.invocation;

import com.eardh.gateway.core.HandlerContext;

/**
 * @author eardh
 * @date 2023/3/26 15:12
 */
public interface RpcInvocation {

    void doInvoke(HandlerContext handlerContext) throws Exception;

}
