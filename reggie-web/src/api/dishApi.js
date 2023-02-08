import request from "../utils/request";

/**
 * 获取分页数据
 * @param { 分页请求参数 } data 
 * @returns 
 */
export function pageApi(data) {
    return request({
        method: 'post',
        url: '/dishes/page',
        data
    })
}

/**
 * 新增菜品
 * @param { 菜品信息 } data 
 * @returns 
 */
export function saveApi(data) {
    return request({
        method: 'post',
        url: '/dishes',
        data
    })
}

/**
 * 修改菜品
 * @param { 菜品信息 } data 
 * @param { 0: 修改菜品信息 1: 修改售卖状态 } flag 
 * @returns 
 */
export function updateApi(data, flag) {
    return request({
        method: 'put',
        url: '/dishes/' + flag,
        data
    })
}

/**
 * 批量修改菜品
 * @param { 菜品列表 } data 
 * @returns 
 */
export function updateBatchApi(data) {
    return request({
        method: 'put',
        url: '/dishes',
        data
    })
}

/**
 * 批量删除菜品
 * @param { 菜品列表 } data 
 * @returns 
 */
export function removeBatchApi(data) {
    return request({
        method: 'delete',
        url: '/dishes',
        data
    })
}

/**
 * 根据分类ID获取菜品列表
 * @param { 菜系分类ID } id 
 * @returns 
 */
export function listApi(id) {
    return request({
        method: 'get',
        url: '/dishes/' + id,
    })
}