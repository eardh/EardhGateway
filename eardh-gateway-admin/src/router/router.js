import Layout from "../layout/Index.vue";
import RouteView from "../components/RouteView.vue";

const layoutMap = [
    {
        path: "",
        name: "Index",
        meta: { title: "控制台", icon: "HomeFilled" },
        component: () => import("../views/Index.vue")
    },
    {
        path: "gateway",
        name: "Gateway",
        component: RouteView,
        meta: { title: " 网关管理", icon: "DataLine" },
        children: [
            {
                path: "gateway-list",
                name: "GatewayServer",
                meta: { title: "网关服务"},
                component: () => import("../views/gateway/Server.vue")
            }
        ]
    },
    {
        path: "application",
        name: "Application",
        meta: { title: "RPC服务管理", icon: "DataLine"},
        component: RouteView,
        children: [
            {
                path: "service-list",
                name: "Microservice",
                meta: { title: "微服务" },
                component: () => import("../views/rpc/Microservice.vue")
            },
            {
                path: "dubbo-route-list",
                name: "DubboRoute",
                meta: { title: "Dubbo服务" },
                component: () => import("../views/rpc/DubboRoute.vue")
            },
            {
                path: "rest-route-list",
                name: "RestRoute",
                meta: { title: "Rest服务" },
                component: () => import("../views/rpc/RestRoute.vue")
            }
        ]
    },
    {
        path: "api",
        name: "Api",
        meta: { title: "API管理", icon: "User"},
        component: RouteView,
        children: [
            {
                path: "api-list",
                name: "ApiService",
                meta: { title: "API服务" },
                component: () => import("../views/api/GatewayApi.vue")
            }
        ]
    },
];

const routes = [
    { path: "/login", name: "Login", meta: { title: "登录" }, component: () => import("../views/login/Login.vue") },
    { path: "/", name: "Layout", component: Layout, children: [...layoutMap] },
];

export { routes, layoutMap };
