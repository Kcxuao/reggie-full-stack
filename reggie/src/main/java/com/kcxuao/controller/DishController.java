package com.kcxuao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcxuao.common.R;
import com.kcxuao.domain.Dish;
import com.kcxuao.domain.DishFlavor;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.dto.DishDto;
import com.kcxuao.service.DishFlavorService;
import com.kcxuao.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping("/page")
    public R<Page<DishDto>> page(@RequestBody PageInfo pageInfo) {
        log.info("请求菜品分页信息 ==> {}", pageInfo);

        Page<DishDto> dishDtoPage = dishService.pageInfo(pageInfo);
        return R.success(dishDtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品 ==> {}", dishDto);

        dishService.saveDish(dishDto);
        return R.success("菜品新增成功");
    }

    @PutMapping("/{flag}")
    public R<String> update(@RequestBody DishDto dishDto, @PathVariable int flag) {
        log.info("修改菜品 ==> {} flag ==> {}", dishDto, flag);

        dishService.updateDish(dishDto, flag);
        return R.success("更新成功");
    }

    @PutMapping
    public R<String> updateBatch(@RequestBody Collection<Dish> dishes) {
        log.info("批量修改 ==> {}", dishes);

        dishService.updateBatchById(dishes);
        return R.success("更新成功");
    }

    @DeleteMapping
    public R<String> removeBatch(@RequestBody Collection<DishDto> dishDtos) {
        log.info("删除 ==> {}", dishDtos);

        dishService.removeBatch(dishDtos);
        return R.success("删除成功");
    }

    @GetMapping("/{value}")
    public R<List<DishDto>> list(@PathVariable Object value) {
        log.info("请求菜品列表 ==> {}", value);

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();

        try {
            value = Long.valueOf((String) value);
            lqw.eq(Dish::getCategoryId, value);
        } catch (NumberFormatException e) {
            lqw.like(Dish::getName, value);
        }

        lqw.eq(Dish::getStatus, 1);
        List<DishDto> dishDtos = new ArrayList<>();
        List<Dish> list = dishService.list(lqw);

        list.forEach(item -> {

            DishDto dishDto = new DishDto();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            BeanUtils.copyProperties(item, dishDto);
            dishDto.setFlavors(flavors);
            dishDtos.add(dishDto);
        });

        return R.success(dishDtos);
    }
}
