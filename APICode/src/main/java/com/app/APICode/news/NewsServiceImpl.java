package com.app.APICode.news;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {
    private NewsRepository newsRepo;

    @Override
    public List<News> listNews() {
        return newsRepo.findAll();
    }

    @Override
    public News getNews(Long id) {
        return newsRepo.findById(id).orElse(null);
    }

    @Override
    public News addNews(News news) {
        List<News> sameNews = newsRepo.findById(news.getId()).map(Collections::singletonList)
                .orElseGet(Collections::emptyList);

        if (sameNews.size() == 0) {
            return newsRepo.save(news);
        } else {
            return null;
        }

    }

    public News updateNews(Long id, News newNews) {
        return newsRepo.findById(id).map(news -> {
            news.setArticleDate(newNews.getArticleDate());
            news.setId(newNews.getId());
            news.setSource(newNews.getSource());
            news.setTitle(newNews.getTitle());
            return newsRepo.save(news);
        }).orElse(null);
    }

    public void deleteNews(Long id) {
        newsRepo.deleteById(id);
    }
}
