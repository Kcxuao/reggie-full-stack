import request from "../utils/request";

/**
 * 登录
 * @param { 账密 } data 
 * @returns 
 */
export function loginApi(data) {
    return request({
        method: "post",
        url: '/employees/login',
        data
    })
}

/**
 * 退出登录
 * @returns 
 */
export function logoutApi() {
    return request({
        method: "get",
        url: '/employees/logout',
    })
}

/**
 * 请求员工分页信息
 * @param { 分页信息 } data 
 * @returns 
 */
export function pageApi(data) {
    return request({
        method: 'post',
        url: '/employees',
        data
    })
}

/**
 * 新增或修改分类信息
 * @param { 分类信息 } data 
 * @param { 0: 新增 1: 修改 } flag 
 * @returns 
 */
export function saveOrUpdateApi(data, flag) {
    return request({
        method: 'post',
        url: `/employees/${flag}`,
        data
    })
}