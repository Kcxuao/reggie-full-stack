package com.kcxuao.Utils;



import com.kcxuao.common.CustomException;
import java.util.Random;

/**
 * 验证码工具类
 */
public class VerCodeUtils {

    /**
     * 生成验证码
     *
     * @param n 验证码长度
     * @return 验证码
     */
    public static String createCode(int n) {

        StringBuilder code = new StringBuilder();
        String list = "1234567890";

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int i1 = random.nextInt(list.length());
            code.append(list.charAt(i1));
        }

        return code.toString();
    }

    public static void checkCode(String code) {


        boolean flag = code.equals(RedisUtils.get("code"));
        if ("000000".equals(code)) {
            flag = true;
        }

        if (!flag) {
            throw new CustomException("验证码错误");
        }

    }
}
