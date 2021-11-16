package com.app.APICode.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Returns HTTP Status Code 204
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentResponse extends RuntimeException {
    public NoContentResponse() {
        super();
    }
}
