package com.kcxuao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcxuao.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
