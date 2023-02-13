
import request from '@/utils/request.js'

export function addressListApi(flag) {
    return request({
        method: 'get',
        url: `/address/list/${flag}`,
    })
}

export function saveOrUpdateApi(data) {
    return request({
        method: 'post',
        url: `/address`,
        data
    })
}

export function setDefaultApi(id) {
    return request({
        method: 'get',
        url: `/address/${id}`,
    })
}

export function removeApi(data) {
    return request({
        method: 'delete',
        url: `/address`,
        data
    })
}
