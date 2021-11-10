package com.app.APICode.notification;

import java.time.LocalDateTime;

public class NotificationNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotificationNotFoundException(LocalDateTime datetime){
        super("Could not find notification on: " + datetime);
    }
}
