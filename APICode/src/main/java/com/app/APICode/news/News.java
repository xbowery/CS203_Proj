package com.app.APICode.news;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class News {

    private @Id @GeneratedValue Long id;

    @NotNull(message = "Source cannot be null")
    private String source;

    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "Article Date cannot be null")
    private Date articleDate;

    @NotNull(message = "URI should not be null")
    private String URI;

    @NotNull()

    public News() {
    }

    public News(String source, String title, Date articleDate, String URI) {
        this.source = source;
        this.title = title;
        this.articleDate = articleDate;
        this.URI = URI;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(Date articleDate) {
        this.articleDate = articleDate;
    }

}
