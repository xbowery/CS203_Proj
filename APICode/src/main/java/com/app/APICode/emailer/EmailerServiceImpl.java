package com.app.APICode.emailer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Implementation of the Emailer service layer
 */
@Service
public class EmailerServiceImpl implements EmailerService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine springTemplateEngine;

    @Value("${application.email}")
    String emailSender;

    public void sendMessage(String recipient, Map<String, Object> variables) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());

        Context context = new Context();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("isRegisterConfirmation", variables.get("isRegisterConfirmation"));
        model.put("isResendConfirmation", variables.get("isResendConfirmation"));
        model.put("requestPasswordChange", variables.get("requestPasswordChange"));

        if ((Boolean)variables.get("isRegisterConfirmation")) {
            model.put("token", variables.get("token"));
        } else if ((Boolean)variables.get("isResendConfirmation")) {
            model.put("token", variables.get("token"));
        } else if ((Boolean)variables.get("requestPasswordChange")) {
            model.put("password", variables.get("password"));
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
        dataModel.put("isResendConfirmation", false);
        dataModel.put("requestPasswordChange", false);

        return dataModel;
    }

    private String getSubjectMessage(Map<String, Object> variables) {
        if ((Boolean) variables.get("isRegisterConfirmation") != false) {
            return "Swisshack Account Registration Confirmation Link";
        } else if ((Boolean) variables.get("isResendConfirmation") != false) {
            return "Swisshack Account Resend Registration Confirmation Link";
        } else if ((Boolean) variables.get("requestPasswordChange") != false) {
            return "Swisshack Account Request for Password Reset";
        }
        return "";
    }
}
