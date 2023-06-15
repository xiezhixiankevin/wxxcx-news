package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.pojo.Comment;
import com.example.demo.pojo.News;
import com.example.demo.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public News getNews(String thumbnail_pic_s, String url, String title, String author_name, String date) {
        // 1.查找该新闻在数据库中是否已经存在
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        News existNews = newsMapper.selectOne(queryWrapper);
        // 1.1. 如果该新闻还未存在则存入数据库并返回
        if (existNews == null) {
            News news = new News();
            news.setThumbnail_pic_s(thumbnail_pic_s);
            news.setUrl(url);
            news.setTitle(title);
            news.setAuthor_name(author_name);
            news.setDate(date);
            newsMapper.insert(news);
            return news;
        } else { // 1.2 如果该新闻已经存在，则返回对应的新闻信息
            return existNews;
        }
    }

    @Override
    public List<Comment> getCommentByNewsUrl(String url) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        Collections.reverse(comments);
        return comments;
    }
}
