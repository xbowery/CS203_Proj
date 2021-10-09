package com.app.APICode.emailer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailerService {

    JavaMailSender javaMailSender;

    SpringTemplateEngine springTemplateEngine;

    @Value("${application.email}")
    String emailSender;

    public void sendMessage(String recipient, Map<String, Object> variables) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());

        Context context = new Context();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("isRegisterConfirmation", variables.get("isRegisterConfirmation"));
        model.put("isDeleted", variables.get("isDeleted"));
        model.put("requestPasswordChange", variables.get("requestPasswordChange"));
        model.put("isUsernameChange", variables.get("isUsernameChange"));
        model.put("isPasswordChange", variables.get("isPasswordChange"));

        if ((Boolean)variables.get("isRegisterConfirmation")) {
            model.put("token", variables.get("token"));
        } else if ((Boolean)variables.get("requestPasswordChange")) {
            model.put("password", variables.get("password"));
        } else if ((Boolean)variables.get("isUsernameChange")) {
            model.put("username", variables.get("username"));
        }

        context.setVariables(model);
        String text = springTemplateEngine.process("emailTemplate", context);

        helper.setTo(recipient);
        helper.setSubject(getSubjectMessage(variables));
        helper.setText(text, true);
        helper.setFrom(emailSender);

        javaMailSender.send(mimeMessage);
    }

    public Map<String, Object> getDataModel() {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("isRegisterConfirmation", false);
        dataModel.put("isDeleted", false);
        dataModel.put("requestPasswordChange", false);
        dataModel.put("isUsernameChange", false);
        dataModel.put("isPasswordChange", false);

        return dataModel;
    }

    private String getSubjectMessage(Map<String, Object> variables) {
        if ((Boolean) variables.get("isRegisterConfirmation") != false) {
            return "COVID Suisse Account Registration Confirmation Link";
        } else if ((Boolean) variables.get("isDeleted") != false) {
            return "COVID Suisse Account Deletion Confirmation";
        } else if ((Boolean) variables.get("requestPasswordChange") != false) {
            return "COVID Suisse Account Request for Password Reset";
        } else if ((Boolean) variables.get("isUsernameChange") != false) {
            return "COVID Suisse Account Username Update";
        } else if ((Boolean) variables.get("isPasswordChange") != false) {
            return "COVID Suisse Account Password Reset";
        }
        return "";
    }
}
