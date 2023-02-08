package com.kcxuao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kcxuao.domain.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {

    List<ShoppingCart> list(long userId);

    void addOrSubNum(long id, int flag, long userId);
}
