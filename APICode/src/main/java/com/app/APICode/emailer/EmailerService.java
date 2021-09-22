package com.app.APICode.emailer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailerService {
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${application.email}")
    String emailSender;

    public void sendMessage(String subject, String text, String recipient) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setFrom(emailSender);

        javaMailSender.send(mimeMessage);
    }
}
