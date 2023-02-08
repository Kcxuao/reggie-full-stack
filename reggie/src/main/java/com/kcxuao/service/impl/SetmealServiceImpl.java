package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcxuao.domain.*;
import com.kcxuao.dto.SetmealDto;
import com.kcxuao.mapper.SetmealMapper;
import com.kcxuao.service.CategoryService;
import com.kcxuao.service.SetmealDishService;
import com.kcxuao.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 分页
     */
    @Override
    public Page<SetmealDto> page(PageInfo pageInfo) {

        Page<Setmeal> setMealPage = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(pageInfo.getName()), Setmeal::getName, pageInfo.getName())
                .orderByDesc(Setmeal::getUpdateTime);

        this.page(setMealPage, setmealLambdaQueryWrapper);
        BeanUtils.copyProperties(setMealPage, setmealDtoPage, "records");

        List<SetmealDto> setmealDtos = setMealPage.getRecords().stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }

            Long id = item.getId();
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
            List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);

            setmealDto.setSetmealDishes(list);
            BeanUtils.copyProperties(item, setmealDto);

            return setmealDto;

        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(setmealDtos);
        return setmealDtoPage;
    }

    /**
     * 保存套餐
     * @param setmealDto
     */
    @Override
    public void saveSetMeal(SetmealDto setmealDto) {

        this.saveOrUpdate(setmealDto);

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getName, setmealDto.getName());
        Setmeal setmeal = this.getOne(lqw);

        if (setmeal != null) {
            Long id = setmeal.getId();
            System.out.println(id);

            List<SetmealDish> list = setmealDto.getSetmealDishes().stream().peek(item -> {
                item.setSetmealId(id);
            }).collect(Collectors.toList());

            setmealDishService.saveOrUpdateBatch(list);
        }

    }

    /**
     * 更新套餐
     * @param setmealDtos
     * @param flag
     */
    @Override
    public void updateSetMeal(Collection<SetmealDto> setmealDtos, int flag) {

        if (flag == 1) {
            for (SetmealDto setmealDto : setmealDtos) {
                this.updateById(setmealDto);
            }
            return;
        }


        SetmealDto setmealDto = setmealDtos.stream().findFirst().orElse(new SetmealDto());
        // 删除菜品表的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(lambdaQueryWrapper);

        this.saveSetMeal(setmealDto);
    }

    /**
     * 批量删除
     * @param setmealDtos
     */
    @Override
    public void removeBatch(Collection<SetmealDto> setmealDtos) {
        for (SetmealDto setmealDto : setmealDtos) {

            if (setmealDto.getStatus() == 1) {
                log.info("当前菜品未禁用，无法删除 ==> {}", setmealDto.getId());
                continue;
            }

            this.removeById(setmealDto);

            LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SetmealDish::getSetmealId, setmealDto.getId());
            setmealDishService.remove(lqw);
        }
    }
}
