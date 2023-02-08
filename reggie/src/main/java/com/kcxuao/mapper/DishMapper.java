package com.kcxuao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcxuao.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
