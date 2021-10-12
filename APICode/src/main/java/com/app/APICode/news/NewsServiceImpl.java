package com.app.APICode.news;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsServiceImpl implements NewsService {
    private NewsRepository newsRepo;

    @Value("${newsapi.uri}")
    private String newsUri;

    @Value("${newsapi.apikey}")
    private String ApiKey;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepo) {
        this.newsRepo = newsRepo;
    }

    @Override
    public List<News> listNews() {
        return newsRepo.findAll();
    }

    @Override
    public News getNews(Long id) {
        return newsRepo.findById(id).orElse(null);
    }

    @Override
    //returns the latest news by article date
    public News getLatestNews() {
        return newsRepo.findTopByOrderByArticleDateDesc();
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

    public void fetchNews() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            URI uri = new URI(newsUri + ApiKey);
            ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
            System.out.println(result.getBody());
        } catch (URISyntaxException e) {
            System.out.println("URI Invalid");
        }
    }
}
