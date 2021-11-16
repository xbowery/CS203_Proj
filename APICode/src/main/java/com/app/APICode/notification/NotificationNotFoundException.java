package com.app.APICode.notification;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a {@link Notification} is not found in the repository
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotificationNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotificationNotFoundException() {
        super("Could not find notification.");
    }
}
