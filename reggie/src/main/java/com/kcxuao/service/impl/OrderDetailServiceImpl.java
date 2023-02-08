package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcxuao.domain.OrderDetail;
import com.kcxuao.mapper.OrderDetailMapper;
import com.kcxuao.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
