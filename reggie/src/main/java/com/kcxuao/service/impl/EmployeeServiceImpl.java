package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcxuao.common.R;
import com.kcxuao.Utils.RedisUtils;
import com.kcxuao.Utils.TokenUtils;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.domain.UserInfo;
import com.kcxuao.mapper.EmployeeMapper;
import com.kcxuao.domain.Employee;
import com.kcxuao.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 员工
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    /**
     * 登录
     * @param employee
     * @return
     */
    @Override
    public R<UserInfo> login(Employee employee) {

        // 将密码进行md5加密
        String md5Password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        employee.setPassword(md5Password);

        // 查询用户是否存在
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername()).eq(Employee::getPassword, employee.getPassword());
        Employee emp = this.getOne(lqw);

        if (emp == null) {
            return R.error("账号或密码错误");
        }

        if (emp.getStatus() == 0) {
            return R.error("账号被锁定");
        }

        // 生成token
        String token = TokenUtils.createToken(String.valueOf(emp.getId()));

        // 将token存入redis
        RedisUtils.createRedis(String.valueOf(emp.getId()), emp.getUsername(), 12L);

        return R.success(new UserInfo(emp.getName(), token));
    }

    /**
     * 获取分页
     * @param pageInfo
     * @return
     */
    @Override
    public Page<Employee> pageInfo(PageInfo pageInfo) {

        Page<Employee> employeePage = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(pageInfo.getName()), Employee::getName, pageInfo.getName());

        return this.page(employeePage, lqw);
    }

    /**
     * 保存或更新
     * @param employee
     */
    @Override
    public void saveOrUpdateEmployee(Employee employee) {
        String md5Password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        String md5Password2 = DigestUtils.md5DigestAsHex(md5Password.getBytes());

        employee.setPassword(md5Password2);
        this.saveOrUpdate(employee);
    }


}
