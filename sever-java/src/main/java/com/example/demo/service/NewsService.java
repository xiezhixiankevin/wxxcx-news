package com.example.demo.service;

import com.example.demo.pojo.Comment;
import com.example.demo.pojo.News;

import java.util.List;

public interface NewsService {
    // 根据一条新闻的相关信息获取更详细的信息，如果该新闻不存在则存入数据库新的信息
    News getNews(String thumbnail_pic_s, String url, String title, String author_name, String date);

    // 根据一条新闻的url找到该新闻所有评论相关的信息
    List<Comment> getCommentByNewsUrl(String url);
}
