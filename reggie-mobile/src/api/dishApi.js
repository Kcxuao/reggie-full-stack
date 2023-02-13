import request from '@/utils/request.js'

/**
 * 获取分类列表
 * @param { 0: 全部分类 1: 菜品分类 2: 套餐分类 } type 
 * @returns 
 */
export function cateListApi(type) {
    return request({
        method: 'get',
        url: `/categories/${type}`,
    })
}

/**
 * 获取菜品列表
 * @param { 分类ID } id 
 * @returns 
 */
export function dishListApi(id) {
    return request({
        method: 'get',
        url: `/dishes/${id}`,
    })
}


export function setMealListApi(id) {
    return request({
        method: 'get',
        url: `/setmeal/${id}`,
    })
}