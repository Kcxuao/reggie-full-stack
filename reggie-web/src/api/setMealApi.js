import request from "../utils/request";

/**
 * 请求员工分页信息
 * @param { 分页信息 } data 
 * @returns 
 */
export function pageApi(data) {
    return request({
        method: 'post',
        url: '/setmeal/page',
        data
    })
}

/**
 * 新增套餐
 * @param { 套餐信息 } data 
 * @returns 
 */
export function saveApi(data) {
    return request({
        method: 'post',
        url: '/setmeal',
        data
    })
}

/**
 * 修改套餐
 * @param { 套餐信息 } data 
 * @param { 0: 修改 1: 修改售卖状态 } flag 
 * @returns 
 */
export function updateApi(data, flag) {
    return request({
        method: 'put',
        url: `/setmeal/${flag}`,
        data
    })
}

/**
 * 批量删除套餐
 * @param { 套餐列表 } data 
 * @returns 
 */
export function removeBatchApi(data) {
    return request({
        method: 'delete',
        url: `/setmeal`,
        data
    })
}