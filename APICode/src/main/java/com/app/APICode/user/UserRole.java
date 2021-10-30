package com.app.APICode.user;

enum UserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), BUSINESS("ROLE_BUSINESS"), EMPLOYEE("ROLE_EMPLOYEE");

    public final String role;

    private UserRole(String role) {
        this.role = role;
    }
}
