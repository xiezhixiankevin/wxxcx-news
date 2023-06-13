package com.example.demo.utils;

import com.example.demo.pojo.Comment;
import com.example.demo.pojo.News;

import java.util.List;

public class NewsWithComment {
    private News news;
    private List<Comment> comments;

    public NewsWithComment(News news, List<Comment> comments) {
        this.news = news;
        this.comments = comments;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
