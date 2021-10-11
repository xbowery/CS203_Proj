package com.app.APICode.news;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {
    private NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public void addNews(News news) {
        newsRepository.save(news);
    }

    @Override
    public List<News> getNews() {
        return newsRepository.findAll();
    }

    @Override
    public List<News> getNews(int limit) {
        return newsRepository.findAll();
    }

    @Override
    public List<News> getNewsByCategory(String category) {
        return newsRepository.findAll();
    }

    @Override
    public List<News> getNewsByCategory(String category, int limit) {
        return newsRepository.findAll();
    }
}
