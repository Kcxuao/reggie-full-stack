package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcxuao.domain.SetmealDish;
import com.kcxuao.mapper.SetmealDishMapper;
import com.kcxuao.service.SetmealDishService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
