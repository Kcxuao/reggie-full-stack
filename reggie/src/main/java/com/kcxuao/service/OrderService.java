package com.kcxuao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kcxuao.domain.Orders;

public interface OrderService extends IService<Orders> {

    void pay(Orders orders);
}
