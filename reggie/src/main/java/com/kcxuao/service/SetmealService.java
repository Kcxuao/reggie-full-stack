package com.kcxuao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.domain.Setmeal;
import com.kcxuao.dto.SetmealDto;

import java.util.Collection;


public interface SetmealService extends IService<Setmeal> {

    Page<SetmealDto> page(PageInfo pageInfo);

    void saveSetMeal(SetmealDto setmealDto);

    void updateSetMeal(Collection<SetmealDto> setmealDtos, int flag);

    void removeBatch(Collection<SetmealDto> setmealDtos);
}
