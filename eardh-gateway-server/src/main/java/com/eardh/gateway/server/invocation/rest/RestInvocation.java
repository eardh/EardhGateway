package com.eardh.gateway.server.invocation.rest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.eardh.gateway.common.util.GateUtils;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.invocation.RpcInvocation;
import com.eardh.gateway.core.util.Result;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.core.wapper.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.eardh.gateway.server.plugin.pre.route.rest.RestRoutePlugin.REST_CALL_STATEMENT;

/**
 * @author eardh
 */
@Slf4j
@Component("restRpcInvocation")
public class RestInvocation implements RpcInvocation {

    @Override
    public void doInvoke(HandlerContext handlerContext) throws Exception {
        Request request = handlerContext.getRequest();
        RestCallStatement callStatement = handlerContext.getAttachment(REST_CALL_STATEMENT);
        String url = GateUtils.buildUri(callStatement.getProtocol(),
                callStatement.getIp(), callStatement.getPort());
        url += callStatement.getUrl();
        HttpResponse httpResponse;
        if (StrUtil.equalsAny(callStatement.getMethod(), "post", "patch", "put")) {
            httpResponse = HttpUtil.createPost(url)
                    .form(request.getParams())
                    .body(JSON.toJSONString(request.getPostBody()))
                    .execute();
        } else {
            httpResponse = HttpUtil.createGet(url)
                    .form(request.getParams())
                    .execute();
        }
        String body = httpResponse.body();
        Result res;
        Response response = handlerContext.getResponse();
        if (JSONUtil.isTypeJSON(body)) {
            JSONObject jsonObject = JSON.parseObject(body);
            if (StrUtil.isNotBlank(jsonObject.getString("token"))) {
                res = Result.ok(jsonObject.getString("token"));
            } else {
                res = Result.ok(jsonObject);
            }
        } else {
            res = Result.ok(body);
        }
        response.setStatusCode(httpResponse.getStatus());
        response.setBody(res);
    }
}
