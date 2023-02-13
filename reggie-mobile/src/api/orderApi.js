import request from '@/utils/request.js'

export function payApi(data) {
    return request({
        method: 'post',
        url: `/orders`,
        data
    })
}

export function listApi() {
    return request({
        method: 'get',
        url: `/orders`,
    })
}

export function encoreApi(id) {
    return request({
        method: 'get',
        url: `/orders/${id}`,
    })
}