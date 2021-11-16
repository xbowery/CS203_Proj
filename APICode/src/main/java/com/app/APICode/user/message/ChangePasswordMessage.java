package com.app.APICode.user.message;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * Java class containing the attributes for the JSON data required for changing password
 */
public class ChangePasswordMessage {
    
    @NotNull
    @Length(min =  8, message = "Current Password needs to be more than 8 Characters long.")
    private String currentPassword;

    @NotNull
    @Length(min =  8, message = "New Password needs to be more than 8 Characters long.")
    private String newPassword;

    @NotNull
    @Length(min =  8, message = "Confirm New Password needs to be more than 8 Characters long.")
    private String cfmPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getCfmPassword() {
        return cfmPassword;
    }
    public void setCfmPassword(String cfmPassword) {
        this.cfmPassword = cfmPassword;
    }
}
