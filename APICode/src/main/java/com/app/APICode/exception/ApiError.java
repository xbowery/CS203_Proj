package com.app.APICode.exception;

import java.util.Date;

public class ApiError {
    final private Date timestamp;
    final private int status;
    final private String error;
    final private String message;
    final private String path;

    public ApiError(final int status, final String error, final String message, final String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        timestamp = new Date();
    }

    public Date getTimeStamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

}
