package com.app.APICode.news;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
// @JsonRootName(value = "articles")
// @JsonIgnoreProperties(ignoreUnknown = true)
public class News {
    public News() {
    }

    @JsonIgnore
    private @Id @GeneratedValue Long id;

    @NotNull(message = "Source cannot be null")
    private String source;

    @JsonProperty("title")
    @NotNull(message = "Title cannot be null")
    private String title;

    @JsonProperty("publishedAt")
    @NotNull(message = "Article Date cannot be null")
    private LocalDateTime articleDate;

    @JsonProperty("url")
    @NotNull(message = "Article URL cannot be null")
    private String articleUrl;

    @JsonProperty("urlToImage")
    @NotNull(message = "Preview Image URL cannot be null")
    private String previewImgUrl;

    @JsonProperty("content")
    @NotNull(message = "Article content cannot be null")
    private String articleContent;

    public News(String source, String title, LocalDateTime articleDate, String articleUrl, String previewImgUrl,
            String articleContent) {
        this.source = source;
        this.title = title;
        this.articleDate = articleDate;
        this.articleUrl = articleUrl;
        this.previewImgUrl = previewImgUrl;
        this.articleContent = articleContent;

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

    @JsonProperty("source")
    public void unpackSource(Map<String, String> source) {
        this.source = source.get("name");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(LocalDateTime articleDate) {
        this.articleDate = articleDate;
    }

    public String getArticleUrl() {
        return this.articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getPreviewImgUrl() {
        return this.previewImgUrl;
    }

    public void setPreviewImgUrl(String previewImgUrl) {
        this.previewImgUrl = previewImgUrl;
    }

    public String getArticleContent() {
        return this.articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
}
