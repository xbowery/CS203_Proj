package com.app.APICode.restaurant;

public class RestrauntOwnerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public RestrauntOwnerNotFoundException(long id) {
        super("Could not find Owner with Restraunt ID: " + id);
    }
}
