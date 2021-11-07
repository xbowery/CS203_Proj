package com.app.APICode.notification;

public class NotificationNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotificationNotFoundException() {
        super("Could not find notification.");
    }
}
