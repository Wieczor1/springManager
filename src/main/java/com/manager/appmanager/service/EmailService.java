package com.manager.appmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {
    @Bean
    @Qualifier("emailSender")
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("in-v3.mailjet.com");
        mailSender.setPort(587);

        mailSender.setUsername("7e685b9008bda20f6b677de1307fb7c9");
        mailSender.setPassword("a04baca89b285c98b698d8102e61aca9");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(@Qualifier("emailSender") @Lazy JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String to,
                         String subject,
                         String text,
                         boolean isHtmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setFrom("tdw146@gmail.com");
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }


}
