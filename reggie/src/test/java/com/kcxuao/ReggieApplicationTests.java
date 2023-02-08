package com.kcxuao;

import com.kcxuao.Utils.EmailUtils;
import com.kcxuao.Utils.TokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    private EmailUtils emailUtils;

    @Test
    void contextLoads() {

        String token = TokenUtils.createToken("2");
        System.out.println(token);
    }

    @Test
    void sendEmail() throws MessagingException {
        String content = "<p>尊敬的用户您好！</p>"
                + "<p style='text-indent:2rem'>" +
                    "您的验证码是：" + 123
                +   " 请在 5 分钟内进行验证。若非本人操作，请无视。"
                + "</p>";

        emailUtils.sendEmail("kcxuao@gmail.com", "瑞吉外卖", content);
    }

}
