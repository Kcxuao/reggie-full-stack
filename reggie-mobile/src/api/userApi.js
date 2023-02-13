import request from '@/utils/request.js'

/**
 * 获取验证码
 * @param { 邮箱 } data 
 * @returns 
 */
export function codeApi(data) {
    return request({
        method: 'post',
        url: '/users/code',
        data
    })
}

/**
 * 登录
 * @param { 邮箱和验证码 } data 
 * @returns 
 */
export function loginApi(data) {
    return request({
        method: 'post',
        url: `/users/login`,
        data
    })
}

export function logoutApi() {
    return request({
        method: 'get',
        url: `/users/logout`,
    })
}