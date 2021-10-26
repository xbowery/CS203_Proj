package com.app.APICode.crowdlevel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CrowdLevelNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CrowdLevelNotFoundException(Long id){
        super("Could not find crowd level with id:  " + id);
    }


}
