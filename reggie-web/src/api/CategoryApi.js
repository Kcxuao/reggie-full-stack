import request from "../utils/request";

/**
 * 请求分类分页信息
 * @param { 分页参数 } data 
 * @returns 
 */
export function pageApi(data) {
    return request({
        method: 'post',
        url: '/categories/page',
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
        url: `/categories/${flag}`,
        data
    })
}

/**
 * 根据ID删除分类
 * @param { 分类ID } id 
 * @returns 
 */
export function removeApi(id) {
    return request({
        method: 'delete',
        url: `/categories/${id}`,
    })
}

/**
 * 请求分类列表
 * @param { 1: 菜品分类 2: 套餐分类 } type 
 * @returns 
 */
export function categoryApi(type) {
    return request({
        method: 'get',
        url: `/categories/${type}`,
    })
}