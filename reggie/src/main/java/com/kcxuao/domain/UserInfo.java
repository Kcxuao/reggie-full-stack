package com.kcxuao.domain;

import lombok.Data;

@Data
public class UserInfo {

    private String identity;

    private String token;

    private Long userId;

    public UserInfo(String identity, String token) {
        this.identity = identity;
        this.token = token;
    }

    public UserInfo(String identity, String token, Long userId) {
        this.identity = identity;
        this.token = token;
        this.userId = userId;
    }
}
