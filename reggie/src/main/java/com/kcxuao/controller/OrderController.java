package com.kcxuao.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kcxuao.common.R;
import com.kcxuao.Utils.RedisUtils;
import com.kcxuao.domain.OrderDetail;
import com.kcxuao.domain.Orders;
import com.kcxuao.domain.ShoppingCart;
import com.kcxuao.dto.OrdersDto;
import com.kcxuao.service.OrderDetailService;
import com.kcxuao.service.OrderService;
import com.kcxuao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 支付
     * @param orders 订单信息
     * @return ok
     */
    @PostMapping
    public R<String> pay(@RequestBody Orders orders) {
        log.info("支付 ==> {}", orders);

        orderService.pay(orders);
        return R.success("支付成功");
    }

    /**
     * 请求订单列表
     * @return 全部订单列表
     */
    @GetMapping
    public R<List<OrdersDto>> list() {
        Long userId = Long.valueOf((String) RedisUtils.get("id"));
        log.info("请求订单列表 ==> {}", userId);

        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Orders::getUserId, userId).orderByDesc(Orders::getCheckoutTime);
        List<Orders> list = orderService.list(lqw);

        List<OrdersDto> ordersDtos = list.stream().map(item -> {

            OrdersDto ordersDto = new OrdersDto();
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLambdaQueryWrapper);

            BeanUtils.copyProperties(item, ordersDto);
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());

        return R.success(ordersDtos);
    }

    /**
     * 再来一单
     * @param id 订单ID
     * @return ok
     */
    @GetMapping("/{id}")
    public R<String> encore(@PathVariable Long id) {
        log.info("再来一单 ==> {}", id);

        Long userId = Long.valueOf((String) RedisUtils.get("id"));

        // 获取当前订单的菜品明细
        LambdaQueryWrapper<OrderDetail> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> orderDetails = orderDetailService.list(lqw);

        List<ShoppingCart> carts = orderDetails.stream().map(item -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 设置购物车对象属性
            shoppingCart.setName(item.getName());
            shoppingCart.setImage(item.getImage());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setPrice(item.getAmount());
            shoppingCart.setUserId(userId);

            if (item.getDishId() != null) {
                shoppingCart.setDishId(item.getDishId());
            } else if (item.getSetmealId() != null) {
                shoppingCart.setSetmealId(item.getSetmealId());
            }

            return shoppingCart;
        }).collect(Collectors.toList());

        // 保存至购物车中
        shoppingCartService.saveBatch(carts);

        return R.success("添加购物车成功");
    }
}