package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcxuao.domain.Category;
import com.kcxuao.domain.Dish;
import com.kcxuao.domain.DishFlavor;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.dto.DishDto;
import com.kcxuao.mapper.DishMapper;
import com.kcxuao.service.CategoryService;
import com.kcxuao.service.DishFlavorService;
import com.kcxuao.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * ่ๅ
 */
@Slf4j
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * ๅ้กต
     * @param pageInfo
     * @return
     */
    @Override
    public Page<DishDto> pageInfo(PageInfo pageInfo) {

        Page<Dish> dishPage = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(pageInfo.getName()), Dish::getName, pageInfo.getName())
                .orderByDesc(Dish::getUpdateTime);
        this.page(dishPage, dishLambdaQueryWrapper);

        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        List<DishDto> dishDtoList = dishPage.getRecords().stream().map(item -> {

            DishDto dishDto = new DishDto();

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            BeanUtils.copyProperties(item, dishDto);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtoList);

        return dishDtoPage;
    }

    /**
     * ไฟๅญ่ๅ
     * @param dishDto
     */
    @Override
    public void saveDish(DishDto dishDto) {

        // ไฟๅญ/ๆดๆฐdish่กจ
        this.saveOrUpdate(dishDto);

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getName, dishDto.getName());
        Dish dish = this.getOne(lambdaQueryWrapper);

        if (dish != null) {
            Long id = dish.getId();
            List<DishFlavor> flavors = dishDto.getFlavors().stream().peek(item -> {
                item.setDishId(id);
            }).collect(Collectors.toList());

            dishFlavorService.saveOrUpdateBatch(flavors);
        }
    }

    /**
     * ๆดๆฐ่ๅ
     * @param dishDto
     * @param flag
     */
    @Override
    public void updateDish(DishDto dishDto, int flag) {

        if (flag == 1) {
            this.updateById(dishDto);
            return;
        }

        // ๅ?้คๅฃๅณ่กจ็ๆฐๆฎ
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        this.saveDish(dishDto);
    }

    /**
     * ๆน้ๅ?้ค่ๅ
     * @param dishes
     */
    @Override
    public void removeBatch(Collection<DishDto> dishes) {

        for (DishDto dish : dishes) {

            if (dish.getStatus() == 1) {
                log.info("ๅฝๅ่ๅๆช็ฆ็จ๏ผๆ?ๆณๅ?้ค ==> {}", dish.getId());
                continue;
            }

            this.removeById(dish);

            LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
            lqw.eq(DishFlavor::getDishId, dish.getId());
            dishFlavorService.remove(lqw);

        }

    }
}
