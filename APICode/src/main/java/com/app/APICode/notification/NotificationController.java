package com.app.APICode.notification;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Notification", description = "Notification API")
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * GET a list of all notifications associated with the user
     * 
     * @return list of all {@link Notification}
     */
    @Operation(summary = "List all of current users notifications", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Notification" })
    @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Notification.class))))
    @GetMapping("/notifications")
    public List<Notification> getNotifications(Principal principal) {
        return notificationService.getNotificationsByUsername(principal.getName());
    }

    /**
     * Functionality to mark all of a user's notifications as read in bulk. It will
     * then return the new list of notifications with all the status being updated
     * 
     * @param principal
     */
    @Operation(summary = "Update all notification status to read", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Notification" })
    @ApiResponse(responseCode = "200", description = "Successful update all notifications to indicate as 'read'", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Notification.class))))
    @PutMapping("/notifications/all")
    public List<Notification> readAllNotifications(Principal principal) {
        return notificationService.markAllNotificationsRead(principal.getName());
    }

    /**
     * Functionality to mark a single notification as read.
     * 
     * @param principal
     */
    @Operation(summary = "Update single notification status to read", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Notification" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful update a single notification to indicate as 'read'"),
            @ApiResponse(responseCode = "404", description = "Notification does not exist", content = @Content), })
    @PutMapping("/notifications/{notificationId}")
    public void readSingleNotification(Principal principal,
            @PathVariable(value = "notificationId") Long notificationId) {
        notificationService.markSingleNotificationRead(principal.getName(), notificationId);
    }

}
