package com.kcxuao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kcxuao.domain.Dish;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.dto.DishDto;

import java.util.Collection;

public interface DishService extends IService<Dish> {

    Page<DishDto> pageInfo(PageInfo pageInfo);

    void saveDish(DishDto dishDto);

    void updateDish(DishDto dishDto, int flag);

    void removeBatch(Collection<DishDto> dishes);
}
