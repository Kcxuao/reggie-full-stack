package com.kcxuao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcxuao.common.R;
import com.kcxuao.Utils.RedisUtils;
import com.kcxuao.Utils.TokenUtils;
import com.kcxuao.domain.Employee;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.domain.UserInfo;
import com.kcxuao.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 员工
 */
@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

    /**
     * 登录
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<UserInfo> Login(@RequestBody Employee employee) {

        log.info("登录 ==> {}", employee);
        return employeeService.login(employee);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public R<String> logout(HttpServletRequest request) {

        // 获取token并解析出用户ID
        String token = request.getHeader("token");
        String id = TokenUtils.verify(token);

        // 根据ID在redis中删除缓存
        RedisUtils.removeRedis(id);

        log.info("退出登录 ==> {}", id);
        return R.success("退出登录成功");
    }

    /**
     * 分页
     * @param pageInfo
     * @return
     */
    @PostMapping
    public R<Page<Employee>> page(@RequestBody PageInfo pageInfo) {
        log.info("请求员工分类 ==> {}", pageInfo);
        Page<Employee> employeePage = employeeService.pageInfo(pageInfo);
        return R.success(employeePage);
    }

    /**
     * 保存 or 更新
     * @param employee
     * @param flag 1：更新 0：保存
     * @return
     */
    @PostMapping("/{flag}")
    public R<String> saveOrUpdate(@RequestBody Employee employee, @PathVariable int flag) {
        log.info("新增员工 or 修改信息 ==> {} flag ==> {}", employee, flag);
        employeeService.saveOrUpdateEmployee(employee);


        if (flag == 1) {
            return R.success("用户状态修改成功");
        }

        return R.success("新增/修改员工成功");

    }
}
