import request from '@/utils/request.js'

/**
 * 获取分类列表
 * @param { 0: 全部分类 1: 菜品分类 2: 套餐分类 } type 
 * @returns 
 */
export function saveApi(data) {
    return request({
        method: 'post',
        url: `/shopping`,
        data
    })
}

export function getCartListApi() {
    return request({
        method: 'get',
        url: `/shopping`,
    })
}

export function updateNumApi(id, flag) {
    return request({
        method: 'put',
        url: `/shopping/${id}/${flag}`,
    })
}

export function cleanApi() {
    return request({
        method: 'delete',
        url: `/shopping/clean`,
    })
}

