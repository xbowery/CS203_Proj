package com.app.APICode.news;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NewsNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private NewsNotFoundException(Long id) {
        super("Could not find news with the id: " + id);
    }
}
