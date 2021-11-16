package com.app.APICode.emailer;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;

/**
 * Service interface layer that helps to send email
 */
public interface EmailerService {

    /**
     * Function to send an email from the application to the recipient.
     * 
     * @param recipient Recipient of the email
     * @param variables Map object containing the strings to be input into the email,
     * including email subject and some variables
     * @throws MessagingException
     * @throws IOException
     */
    public void sendMessage(String recipient, Map<String, Object> variables) throws MessagingException, IOException;

    /**
     * Contains the subject of the email to be sent out by the application
     * 
     * @return {@link Map} object containing the variables
     */
    public Map<String, Object> getDataModel();
}
