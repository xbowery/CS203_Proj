package com.app.APICode.news;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsController {
    private NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public List<News> retrieveAllNews() {
        return newsService.listNews();
    }

    @GetMapping("/news/{id}")
    public News getNews(@PathVariable Long id){
        News news = newsService.getNews(id);

         if (news== null) throw new NewsNotFoundException(id);
         return newsService.getNews(id);
    }

    @GetMapping("/news/latest")
    public News getLatestNews(){
        return newsService.getLatestNews();
    }

    /**
    * @param news
    * @return
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/news")
    public News addNews(@Valid @RequestBody News news){
        News savedNews = newsService.addNews(news);
        return savedNews;

    }

    /**
     * If there is no measure with the given creationDateTime, throw MeasureHawkerNotFoundException
     * @param creationDateTime
     * @param newMeasureInfo
     * @return the updated measure
     */
    @PutMapping("/news/{id}")
    public News updateNews(@PathVariable Long id, @Valid @RequestBody News newNewsInfo){
        News news = newsService.updateNews(id, newNewsInfo);
        if (news == null) throw new NewsNotFoundException(id);

        return news;
    }


    @Transactional
    @DeleteMapping("/news/{id}")
    public void deleteNews(@PathVariable Long id){
        try {
            newsService.deleteNews(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NewsNotFoundException(id);
        }
    }
}
