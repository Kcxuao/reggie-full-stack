package com.kcxuao.common;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.NestedServletException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局错误拦截
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * token解析错误
      * @param ex
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = {JWTDecodeException.class, IllegalArgumentException.class, SignatureVerificationException.class, NestedServletException.class})
    public R<String> JWTDecodeExceptionHandler(Exception ex) throws IOException {

        log.error("token解析异常 ==> {}", ex.getMessage());
        return R.error("TOKEN无效");
    }

    /**
     * sql错误
     * @param ex
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public  R<String> SQLIntegrityConstraintViolationExceptionHandler(Exception ex) throws IOException {

        log.error("SQL异常==> {}", ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {

            String s = ex.getMessage().split("Duplicate entry")[1].split(" for key")[0].replace("'", "");
            return R.error("「 " + s + " 」已存在");
        }

        return R.error("未知错误");
    }

    /**
     * 自定义业务异常
     * @param ex
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = {CustomException.class})
    public  R<String> CustomExceptionHandler(Exception ex) throws IOException {

        log.error("业务异常 ==> {}", ex.getMessage());
        return R.error(ex.getMessage());
    }

    /**
     * 油件发送失败
     * @param ex
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = {MessagingException.class})
    public R<String> MessagingExceptionHandler(Exception ex) throws IOException {

        log.error("邮件发送失败 ==> {}", ex.getMessage());
        return R.error("发送失败，请稍后再试");
    }
}
