package com.kcxuao.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件
 */
@Component
@Slf4j
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${reggie.mailFrom}")
    private String from;

    /**
     * 发送邮件
     * @param to
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;

        messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        message.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
        //日志信息
        log.info("邮件已经发送。");
    }
}
