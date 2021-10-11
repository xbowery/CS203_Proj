package com.app.APICode.news;

import java.util.List;

public interface NewsService {
    public List<News> listNews();

    News getNews(Long id);

    News addNews(News news);

    News updateNews(Long id, News news);

    void deleteNews(Long id);
}
