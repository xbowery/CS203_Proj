package com.app.APICode.verificationtoken;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import com.app.APICode.emailer.EmailerService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    
    private VerificationTokenRepository vTokenRepository;

    private EmailerService emailerService;

    private UserService userService;
    
    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Autowired
    private VerificationTokenServiceImpl(VerificationTokenRepository vTokenRepository, EmailerService emailerService, UserService userService) {
        this.vTokenRepository = vTokenRepository;
        this.emailerService = emailerService;
        this.userService = userService;
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return vTokenRepository.findByToken(VerificationToken).orElse(null);
    }

    @Override
    public VerificationToken createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        return vTokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = vTokenRepository.findByToken(existingVerificationToken).orElse(null);
        vToken.updateToken(UUID.randomUUID().toString());
        return vTokenRepository.save(vToken);
    }

    /**
     * Add logic to validate verification token by calculating the expiry date of
     * token
     */
    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken vToken = getVerificationToken(token);
        if (vToken == null) {
            return TOKEN_INVALID;
        }

        final User user = vToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((vToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            VerificationToken newVToken = generateNewVerificationToken(token);

            Map<String, Object> dataModel = emailerService.getDataModel();
            dataModel.put("isResendConfirmation", true);
            dataModel.put("token", "http://localhost:3000/RegisterConfirmation?token=" + newVToken);

            // try {
            // emailerService.sendMessage(user.getEmail(), dataModel);
            // } catch (MessagingException e) {
            // System.out.println("Error occurred while trying to send an email to: " +
            // user.getEmail());
            // } catch (IOException e) {
            // System.out.println("Error occurred while trying to send an email to: " +
            // user.getEmail());
            // }
            
            return TOKEN_EXPIRED;
        }
        user.setEnabled(true);
        userService.save(user);
        return TOKEN_VALID;
    }

}
