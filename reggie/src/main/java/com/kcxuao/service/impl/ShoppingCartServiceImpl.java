package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kcxuao.common.CustomException;
import com.kcxuao.domain.ShoppingCart;
import com.kcxuao.mapper.ShoppingCartMapper;
import com.kcxuao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Override
    @Cacheable(value = "ShoppingCache", key = "#userId")
    public List<ShoppingCart> list(long userId) {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId).orderByAsc(ShoppingCart::getCreateTime);
        return shoppingCartService.list(lqw);
    }

    @Override
    @CacheEvict(value = "ShoppingCache", key = "#userId")
    public void addOrSubNum(long id, int flag, long userId) {
        LambdaUpdateWrapper<ShoppingCart> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();

        // 判断ID是否存在
        ShoppingCart shop = shoppingCartService.getById(id);
        if (shop == null) {
            throw new CustomException("id不存在");
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
                this.removeById(id);
            }
        }

        lambdaUpdateWrapper.eq(ShoppingCart::getId, id);
        shoppingCartService.update(lambdaUpdateWrapper);
    }
}
