package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcxuao.domain.DishFlavor;
import com.kcxuao.mapper.DishFlavorMapper;
import com.kcxuao.service.DishFlavorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
