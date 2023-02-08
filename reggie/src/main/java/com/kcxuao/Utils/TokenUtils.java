package com.kcxuao.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;


/**
 * Token工具类
 */
@Component
public class TokenUtils {
    public static final String SECRET = "ASD!@#F^%A";


    public static String createToken(String id) {
        HashMap<String, Object> headers = new HashMap<>();

        return JWT.create()
                // 第一部分Header
                .withHeader(headers)
                // 第二部分Payload
                .withClaim("id", id)
                .withClaim("time", new Date())
                // 第三部分Signature
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static String verify(String token) {
        // 创建一个验证的对象
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        return verify.getClaim("id").asString();

    }

}


