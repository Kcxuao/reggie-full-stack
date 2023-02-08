package com.kcxuao.common;

/**
 * 自定义业务错误类
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
