package com.kcxuao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcxuao.common.R;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.domain.Setmeal;
import com.kcxuao.domain.SetmealDish;
import com.kcxuao.dto.SetmealDto;
import com.kcxuao.service.SetmealDishService;
import com.kcxuao.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping("/page")
    public R<Page<SetmealDto>> page(@RequestBody PageInfo pageInfo) {
        log.info("请求套餐分页信息 ==> {}", pageInfo);

        Page<SetmealDto> page = setmealService.page(pageInfo);
        return R.success(page);
    }

    /**
     * 保存
     * @param setmealDto 套餐DTO类
     * @return ok
     */
    @PostMapping
    @CacheEvict(value = "SetMealCache", key = "#setmealDto.categoryId")
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐 ==> {}", setmealDto);

        setmealService.saveSetMeal(setmealDto);
        return R.success("新增成功");
    }

    /**
     * 更新
     * @param setmealDtos 套餐DTO类列表
     * @param flag 0: 删除 1: 修改
     * @return ok
     */
    @PutMapping("/{flag}")
    @CacheEvict(value = "SetMealCache", key = "#setmealDtos.get(0).categoryId")
    public R<String> update(@RequestBody List<SetmealDto> setmealDtos, @PathVariable int flag) {
        log.info("修改套餐 ==> {} flag ==> {}", setmealDtos, flag);

        setmealService.updateSetMeal(setmealDtos, flag);
        return R.success("修改成功");
    }

    /**
     * 批量删除
     * @param setmealDtos 套餐DTO类列表
     * @return ok
     */
    @DeleteMapping
    public R<String> removeBatch(@RequestBody Collection<SetmealDto> setmealDtos) {
        log.info("删除 ==> {}", setmealDtos);

        setmealService.removeBatch(setmealDtos);
        return R.success("删除成功");
    }

    /**
     * 获取套餐列表
     * @param id 分类ID
     * @return 套餐DTO类列表
     */
    @GetMapping("{id}")
    @Cacheable(value = "SetMealCache", key = "#id", unless = "#result == null")
    public R<List<SetmealDto>> list(@PathVariable Long id) {
        log.info("获取套餐列表 ==> {}", id);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getCategoryId, id).eq(Setmeal::getStatus, 1);
        List<Setmeal> setmeals = setmealService.list(lqw);

        List<SetmealDto> list = setmeals.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();

            LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> setmealDishList = setmealDishService.list(dishLambdaQueryWrapper);

            setmealDto.setSetmealDishes(setmealDishList);
            BeanUtils.copyProperties(item, setmealDto);
            return setmealDto;
        }).collect(Collectors.toList());
        return R.success(list);
    }
}
