package com.kcxuao.interceptor;

import com.alibaba.fastjson.JSON;
import com.kcxuao.common.R;
import com.kcxuao.Utils.RedisUtils;
import com.kcxuao.Utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截成功 ==> {}", request.getRequestURI());

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Methods","*");

        String url = request.getRequestURI();
        String method = request.getMethod();

        boolean flag =  "OPTION".equals(method)
                || "/employees/logout".equals(url)
                || url.contains("/files/")
                || url.contains("login")
                || url.contains("code");

        if (flag) {
            return true;
        }

        String token = request.getHeader("token");

        if (token == null) {
            response.getWriter().write(JSON.toJSONString(R.error("token不存在")));
            return false;
        }

        String id = TokenUtils.verify(token);
        String value = RedisUtils.get(id);

        if (value == null) {
            response.getWriter().write(JSON.toJSONString(R.error("token无效")));
            return false;
        }

        RedisUtils.createRedis("id", id, 12L);
        return true;
    }
}
