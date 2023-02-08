package com.kcxuao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kcxuao.common.R;
import com.kcxuao.domain.Employee;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.domain.UserInfo;


public interface EmployeeService extends IService<Employee> {

    R<UserInfo> login(Employee employee);

    Page<Employee> pageInfo(PageInfo pageInfo);

    void saveOrUpdateEmployee(Employee employee);


}
