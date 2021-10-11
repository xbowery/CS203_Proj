package com.app.APICode.news;

import java.util.List;

public interface NewsService {
    public void addNews(News news);

    public List<News> getNews();

    public List<News> getNews(int limit);

    public List<News> getNewsByCategory(String category);

    public List<News> getNewsByCategory(String category, int limit);
}
