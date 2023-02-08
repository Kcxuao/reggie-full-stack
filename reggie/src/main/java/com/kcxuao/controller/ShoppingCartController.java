package com.kcxuao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kcxuao.common.R;
import com.kcxuao.Utils.RedisUtils;
import com.kcxuao.domain.ShoppingCart;
import com.kcxuao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shopping")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 保存 or 修改
     * @param shoppingCart 购物车数据
     * @return 购物车数据
     */
    @PostMapping
    @CacheEvict(value = "ShoppingCache", key = "#shoppingCart.userId")
    public R<ShoppingCart> saveOrUpdate(@RequestBody ShoppingCart shoppingCart) {
        log.info("新增购物车 ==> {}", shoppingCart);

        if (shoppingCart.getNumber() < 1) {
            return R.error("数量非法");
        }

        // 查询菜品是否存在
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());

        if (dishId != null) {
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart shop = shoppingCartService.getOne(lambdaQueryWrapper);
        if (shop != null) {
            shop.setNumber(shop.getNumber() + 1);
            shoppingCartService.updateById(shop);
        } else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shop = shoppingCart;
        }

        return R.success(shop);
    }

    /**
     * 购物车列表
     * @return 购物车商品列表
     */
    @GetMapping
    public R<List<ShoppingCart>> list() {
        long userId = Long.parseLong((String) RedisUtils.get("id"));
        log.info("获取购物车列表 ==> {}", userId);

        List<ShoppingCart> list = shoppingCartService.list(userId);
        return R.success(list);
    }

    /**
     * 增加 or 减少数量
     * @param id 菜品 or 套餐ID
     * @param flag 0：数量减一 1：数量加一
     * @return ok
     */
    @PutMapping("/{id}/{flag}")
    public R<String> addOrSubNum(@PathVariable Long id, @PathVariable int flag) {
        long userId = Long.parseLong((String) RedisUtils.get("id"));
        shoppingCartService.addOrSubNum(id, flag, userId);
        return R.success("ok");
    }

    /**
     * 删除购物车
     * @param id 菜品 or 套餐ID
     * @return ok
     */
    @DeleteMapping("/{id}")
    @CacheEvict(value = "ShoppingCache", key = "#id")
    public R<String> remove(@PathVariable Long id) {
        log.info("删除购物车列表 ==> {}", id);
        shoppingCartService.removeById(id);
        return R.success("删除成功");
    }

    /**
     * 清空购物车
     * @return ok
     */
    @DeleteMapping("/clean")
    @CacheEvict(value = "ShoppingCache", allEntries = true)
    public R<String> clean() {
        Long userId = Long.valueOf((String) RedisUtils.get("id"));
        log.info("清空购物车 ==> {}", userId);

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(lqw);
        return R.success("购物车清空成功");
    }

}
