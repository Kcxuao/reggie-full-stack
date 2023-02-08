package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcxuao.common.CustomException;
import com.kcxuao.domain.Category;
import com.kcxuao.domain.Dish;
import com.kcxuao.domain.Orders;
import com.kcxuao.domain.Setmeal;
import com.kcxuao.mapper.CategoryMapper;
import com.kcxuao.service.CategoryService;
import com.kcxuao.service.DishService;
import com.kcxuao.service.OrderService;
import com.kcxuao.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类ID删除分类
     * @param id
     */
    @Override
    public void delete(Long id) {

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(lambdaQueryWrapper);

        LambdaQueryWrapper<Setmeal> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(ordersLambdaQueryWrapper);

        if (count > 0 || count1 > 0) {
            throw new CustomException("当前分类下存在菜品或套餐，无法删除");
        }

        this.removeById(id);
    }
}
