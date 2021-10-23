package com.app.APICode.task;

import com.app.APICode.passwordresettoken.PasswordResetTokenRepository;
import com.app.APICode.verificationtoken.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class TokenPurgingTask {
    
    VerificationTokenRepository vTokens;

    PasswordResetTokenRepository pTokens;

    @Autowired
    public TokenPurgingTask(VerificationTokenRepository vTokens, PasswordResetTokenRepository pTokens) {
        this.vTokens = vTokens;
        this.pTokens = pTokens;
    }

    @Scheduled(cron = "${purging.cron.expression}")
    public void purgeExpiredTokens() {
        Date now = Date.from(Instant.now());

        vTokens.deleteAllExpiredSince(now);
        pTokens.deleteAllExpiredSince(now);
    }
}
