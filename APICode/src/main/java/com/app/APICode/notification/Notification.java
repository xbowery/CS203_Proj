package com.app.APICode.notification;

import java.net.URL;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.app.APICode.user.User;

@Entity
public class Notification {
    private @Id @GeneratedValue Long id;

    @NotNull(message = "datetime cannot be null")
    private LocalDateTime datetime;

    @NotNull(message = "text cannot be null")
    private String text;

    @NotNull(message = "link cannot be null")
    private URL link;

    @NotNull(message = "seen cannot be null")
    private boolean isSeen;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Notification() {}

    public LocalDateTime getDate() {
        return datetime;
    }

    public void setDate(LocalDateTime datetime) {
        this.datetime= datetime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

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

    public Notification(LocalDateTime datetime,String text, URL link, boolean isSeen) {
        this.datetime = datetime;
        this.text = text;
        this.link = link;
        this.isSeen = isSeen;
    }

}
