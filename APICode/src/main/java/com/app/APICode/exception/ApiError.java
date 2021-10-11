package com.app.APICode.exception;

public class ApiError {
    final private int status;
    final private String message;
    final private String error;

    public ApiError(final int status, final String error, final String message) {
        super();
        this.status = status;
        this.error = error;
        this.message = message;
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

}
