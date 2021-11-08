package com.app.APICode.notification;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
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
     * @return list of all {@link Notfication}
     */
    @Operation(summary = "List all of current users notifications", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Notification" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Notification.class)))), })
    @GetMapping("/user/notificaitons")
    public List<Notification> getNotifcations(Principal principal) {
        return notificationService.getNotificationsByUsername(principal.getName());
    }

}
