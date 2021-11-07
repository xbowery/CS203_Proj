package com.app.APICode.notification;

import java.net.URL;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.app.APICode.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Notification {
    private @Id @GeneratedValue Long id;

    @NotNull(message = "datetime cannot be null")
    private LocalDate datetime;

    @NotNull(message = "text cannot be null")
    private String text;

    // @NotNull(message = "link cannot be null")
    // private URL link;

    @NotNull(message = "seen cannot be null")
    private boolean isSeen = false;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonBackReference
    @Schema(description = "User that this Notifications belongs to.", required = true)
    private User user;

    public Notification() {
    }

    public Notification(String text, User user) {
        this.datetime = LocalDate.now();
        // this.link = link;
        this.text = text;
        this.user = user;
    }

    public LocalDate getDate() {
        return datetime;
    }

    public void setDate(LocalDate datetime) {
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
