package com.kcxuao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kcxuao.common.R;
import com.kcxuao.Utils.RedisUtils;
import com.kcxuao.domain.ShoppingCart;
import com.kcxuao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param shoppingCart
     * @return
     */
    @PostMapping
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
     * @return
     */
    @GetMapping
    public R<List<ShoppingCart>> list() {
        Long userId = Long.valueOf(RedisUtils.get("id"));
        log.info("获取购物车列表 ==> {}", userId);

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId).orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);
    }

    /**
     * 增加 or 减少数量
     * @param id
     * @param flag
     * @return
     */
    @PutMapping("/{id}/{flag}")
    public R<String> addOrSubNum(@PathVariable Long id, @PathVariable int flag) {
        LambdaUpdateWrapper<ShoppingCart> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();

        // 判断ID是否存在
        ShoppingCart shop = shoppingCartService.getById(id);
        if (shop == null) {
            return R.success("id不存在");
        }
        if (flag == 1) {
            // 增加
            log.info("商品数量+1 ==> {}", id);
            lambdaUpdateWrapper.setSql("number = number + 1");
        } else {
            // 减少
            log.info("商品数量-1 ==> {}", id);
            lambdaUpdateWrapper.setSql("number = number - 1");

            if (shop.getNumber() <= 1) {
                this.remove(id);
            }
        }

        lambdaUpdateWrapper.eq(ShoppingCart::getId, id);
        shoppingCartService.update(lambdaUpdateWrapper);

        return R.success("ok");
    }

    /**
     * 删除购物车
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R<String> remove(@PathVariable Long id) {
        log.info("删除购物车列表 ==> {}", id);
        shoppingCartService.removeById(id);
        return R.success("删除成功");
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        Long userId = Long.valueOf(RedisUtils.get("id"));
        log.info("清空购物车 ==> {}", userId);

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(lqw);
        return R.success("购物车清空成功");
    }

}
