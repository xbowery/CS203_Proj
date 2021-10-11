package com.app.APICode.news;

public class NewsNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private NewsNotFoundException(Long id){
        super("Could not find news with the id: " + id);
    }
}
