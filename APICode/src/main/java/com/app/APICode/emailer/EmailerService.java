package com.app.APICode.emailer;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;

public interface EmailerService {

    public void sendMessage(String recipient, Map<String, Object> variables) throws MessagingException, IOException;

    public Map<String, Object> getDataModel();
}
