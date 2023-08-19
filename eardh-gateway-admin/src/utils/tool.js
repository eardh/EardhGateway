/* 处理权限 */
import {ElMessage} from "element-plus";

export const hasPermission = (route, role) => {
    if (route.meta && route.meta.roles) {
        return route.meta.roles.includes(role);
    }
    return true; /* 默认不设权限 */
};

export const filterAsyncRouter = (routerMap, roles) => {
    const accessedRouters = routerMap.filter(route => {
        if (hasPermission(route, roles)) {
            if (route.children && route.children.length) {
                route.children = filterAsyncRouter(route.children, roles);
            }
            return true;
        }
        return false;
    });
    return accessedRouters;
};

export const formatter = (row, column, cellValue) => {
    let date = new Date(cellValue);
    let year = date.getUTCFullYear();
    let month = date.getUTCMonth() + 1;
    let day = date.getUTCDate();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    return year + "-" + month + "-" + day;
}

// 下面为消息提示
export const successTip = () => {
    ElMessage({
        message: '操作成功',
        type: 'success',
    })
}
export const errorTip = () => {
    ElMessage.error('操作失败')
}

export const toFormData = (arr) => {
    let formData = new FormData()
    if (arr != null && arr.length != 0) {
        arr.forEach(e => {
            formData.append(e.name, e.value)
        })
    }
    return formData;
}

export const toUrlData = (arr) => {
    let urlData = {}
    if (arr != null && arr.length != 0) {
        arr.forEach(e => {
            urlData[e.name] = e.value
        })
    }
    return urlData;
}
