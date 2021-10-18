package com.app.APICode.crowdlevel;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CrowdLevelNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CrowdLevelNotFoundException(Date datetime){
        super("Could not find crowd level on" + datetime);
    }
}
