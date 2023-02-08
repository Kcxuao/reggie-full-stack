package com.kcxuao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kcxuao.Utils.EmailUtils;
import com.kcxuao.Utils.RedisUtils;
import com.kcxuao.Utils.TokenUtils;
import com.kcxuao.Utils.VerCodeUtils;
import com.kcxuao.common.*;
import com.kcxuao.domain.User;
import com.kcxuao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @return
     * @throws MessagingException
     */
    @PostMapping("/code")
    public R<String> sendCode(@RequestBody User user) throws MessagingException {
        log.info("获取验证码 ==> {}", user.getEmail());

        if (!StringUtils.isNotEmpty(user.getEmail())) {
            return R.error("邮箱为空");
        }

        // 生成验证码
        String code = VerCodeUtils.createCode(6);

        // 将验证码放入redis缓存
        RedisUtils.createRedis("code", code, 5L, TimeUnit.MINUTES);

        // 邮件模版
        String content = "<p>尊敬的用户您好！</p>"
                + "<p style='text-indent:2rem'>" +
                    "您的验证码是：" + code
                +   " 请在 5 分钟内进行验证。若非本人操作，请无视。"
                + "</p>";

        // 发送邮件
        emailUtils.sendEmail(user.getEmail(), "瑞吉外卖", content);
        return R.success("发送成功");
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody User user) {
        log.info("登录 ==> {}", user);

        // 验证验证码
        VerCodeUtils.checkCode(user.getCode());

        // 查找用户
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, user.getEmail());
        User user1 = userService.getOne(lqw);

        // 用户不存在则自动注册
        if (user1 == null) {
            user1 = new User();
            user1.setName("用户" + UUID.randomUUID());
            user1.setEmail(user.getEmail());
            user1.setStatus(1);

            userService.save(user1);
        }

        // 生成token
        String token = TokenUtils.createToken(String.valueOf(user1.getId()));

        // 清除验证码
        RedisUtils.removeRedis("code");

        // 将token存入redis
        RedisUtils.createRedis(String.valueOf(user1.getId()), user1.getEmail(), 12L);
        user1.setToken(token);
        return R.success(user1);
    }

    /**
     * 用户退出登录
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


}
