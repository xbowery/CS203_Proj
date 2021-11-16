package com.app.APICode.notification;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.app.APICode.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * POJO that stores the details of a {@link Notification} in the application
 */
@Entity
public class Notification {
    private @Id @GeneratedValue Long id;

    @NotNull(message = "datetime cannot be null")
    @Schema(description = "Date and time that the notification was created", required = true)
    private LocalDateTime datetime;

    @NotNull(message = "text cannot be null")
    @Schema(description = "Text in the notification", required = true)
    private String text;

    @NotNull(message = "seen cannot be null")
    @Schema(description = "Boolean value if the notification has been seen by user", required = true)
    private boolean isSeen = false;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonBackReference
    @Schema(description = "User that this Notifications belongs to.", required = true)
    private User user;

    public Notification() {
    }

    public Notification(String text, User user) {
        this.text = text;
        this.user = user;
        datetime = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return datetime;
    }

    public void setDate(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // public URL getLink() {
    // return link;
    // }

    // public void setLink(URL link) {
    // this.link = link;
    // }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

}
