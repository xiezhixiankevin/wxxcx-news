package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.pojo.Comment;
import com.example.demo.pojo.News;
import com.example.demo.service.NewsService;
import com.example.demo.utils.NewsWithComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    // 获取一条新闻相关信息
    @PostMapping("/getNews")
    public R<?> getNews(@RequestParam String thumbnail_pic_s,
                        @RequestParam String url,
                        @RequestParam String title,
                        @RequestParam String author_name,
                        @RequestParam String date) {
        // 1 获取新闻除评论和内容外的信息
        News news = newsService.getNews(thumbnail_pic_s, url, title, author_name, date);
        // 2 获取新闻评论相关信息
        List<Comment> comments = newsService.getCommentByNewsUrl(url);

        return R.ok(new NewsWithComment(news, comments));
    }
}
