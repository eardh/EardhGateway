import request from '../utils/axios'
import axios from "axios";
import {errorTip, toFormData, toUrlData} from "@/utils/tool";


export const doRequestDebugger = (url, apiMethod, headers, params, formBody, rowBody, contentType) => {
    let request = axios.create({
        baseURL: 'http://127.0.0.1:9527',
        timeout: 10000,
        headers: toUrlData(headers)
    });
    let response;
    switch (apiMethod) {
        case 'get':
            response =  request.get(url, {
                params: toUrlData(params)
            })
            break
        case 'post':
            if (contentType == 'formType') {
                response = request.post(url, toFormData(formBody), {
                    params: toUrlData(params)
                })
            } else {
                response = request.post(url, rowBody.replace(/\s/g, ''), {
                    params: toUrlData(params),
                    headers: {
                        'content-type': 'application/json'
                    }
                })
            }
            break
        case 'delete':
            response = request.delete(url, {
                params: toUrlData(params)
            })
            break
        case 'put':
            if (contentType == 'formType') {
                response = request.put(url, toFormData(formBody), {
                    params: toUrlData(params)
                })
            } else {
                 response = request.put(url, rowBody.replace(/\s/g, ''), {
                    params: toUrlData(params),
                    headers: {
                        'content-type': 'application/json'
                    }
                })
            }
            break
        default:
            errorTip()
    }
    return response;
}

export const fetchApplicationData = (currentPage, pageSize) => {
    return request.get("wg/admin/rpc/microservice-list", {
        params: {
            currentPage: currentPage,
            pageSize: pageSize
        }
    })
}
export const updateApplicationData = (form) => {
    return request.post("/wg/admin/rpc/register-microservice", {...form})
}

export const fetchOnlineApplicationInstanceData = (microserviceId) => {
    return request.get("/wg/admin/rpc/application-instances", {
        params: {
            microserviceId: microserviceId
        }
    })
}

export const fetchDubboRouteData = (currentPage, pageSize) => {
    return request.get("/wg/admin/rpc/dubbo-route-list", {
        params: {
            currentPage: currentPage,
            pageSize: pageSize
        }
    })
}

export const updateDubboRouteData = (form) => {
    return request.post("/wg/admin/rpc/update-dubbo-route", {...form})
}

export const fetchRestRouteData = (currentPage, pageSize) => {
    return request.get("/wg/admin/rpc/rest-route-list", {
        params: {
            currentPage: currentPage,
            pageSize: pageSize
        }
    })
}

export const updateRestRouteData = (form) => {
    return request.post("/wg/admin/rpc/update-rest-route", {...form})
}


export const fetchGatewayServerData = (currentPage, pageSize) => {
    return request.get("/wg/admin/gateway/gateway-list", {
        params: {
            currentPage: currentPage,
            pageSize: pageSize
        }
    })
}

export const fetchOnlineGatewayInstanceData = (gatewayId) => {
    return request.get("/wg/admin/rpc/gateway-instances", {
        params: {
            gatewayId: gatewayId
        }
    })
}

export const updateGatewayServerData = (form) => {
    return request.post("/wg/admin/gateway/register-gateway", {...form})
}


export const fetchGatewayApiData = (currentPage, pageSize) => {
    return request.get("/wg/admin/api/api-list", {
        params: {
            currentPage: currentPage,
            pageSize: pageSize
        }
    })
}

export const updateGatewayApiData = (form) => {
    return request.post("/wg/admin/api/register-api", {...form})
}

export const fetchGatewayApiByApiId = (apiId) => {
    return request.get("/wg/admin/api/api-by_apiId", {
        params: {
            apiId: apiId
        }
    })
}

export const fetchRateLimiterData = (apiId) => {
    return request.get("/wg/admin/api/rate_limiter-by-apiId", {
        params: {
            apiId: apiId
        }
    })
}

export const updateRateLimiterData = (form) => {
    return request.post("/wg/admin/api/register-rate_limiter", {...form})
}

export const fetchWhiteListData = (apiId) => {
    return request.get("/wg/admin/api/ip_whitelist-by-apiId", {
        params: {
            apiId: apiId
        }
    })
}

export const updateWhiteListData = (form) => {
    return request.post("/wg/admin/api/register-ip_whitelist", {...form})
}


export const fetchBlackListData = (apiId) => {
    return request.get("/wg/admin/api/ip_blacklist-by-apiId", {
        params: {
            apiId: apiId
        }
    })
}

export const updateBlackListData = (form) => {
    return request.post("/wg/admin/api/register-ip_blacklist", {...form})
}



