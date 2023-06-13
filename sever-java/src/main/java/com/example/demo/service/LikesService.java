package com.example.demo.service;

import com.example.demo.utils.contentNewsList;

import java.util.List;

public interface LikesService {
    // 处理用户点赞行为，type为1表示这是个点赞行为，为-1表明这是个取消点赞的行为
    boolean kudos(String openid, String url, int type);

    // 获取对应用户的所有点赞过的新闻信息
    List<contentNewsList> getLikes(String openid);

    boolean ifLiked(String url, String openid);
}
