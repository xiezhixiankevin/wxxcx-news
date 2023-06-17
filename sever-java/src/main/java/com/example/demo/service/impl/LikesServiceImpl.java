package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.mapper.LikesMapper;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.Collection;
import com.example.demo.pojo.Likes;
import com.example.demo.pojo.News;
import com.example.demo.pojo.User;
import com.example.demo.service.LikesService;
import com.example.demo.utils.contentNewsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikesServiceImpl implements LikesService {
    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LikesMapper likesMapper;
    @Override
    public boolean kudos(String openid, String url, int type) {
        // 1 点赞行为
        if (type == 1) {
            // 1.1 更新news表中对应点赞数的信息
            QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
            newsQueryWrapper.eq("url", url);
            News news = newsMapper.selectOne(newsQueryWrapper);
            if (news == null) {
                return false;
            }
            news.setLikes(news.getLikes() + 1);
            UpdateWrapper<News> newsUpdateWrapper = new UpdateWrapper<>();
            newsUpdateWrapper.eq("url", url);
            newsMapper.update(news, newsUpdateWrapper);

            // 1.2 新插入likes表一条信息
            Likes likes = new Likes();
            likes.setUrl(url);
            likes.setTitle(news.getTitle());
            likes.setOpenid(openid);
            likes.setAuthor_name(news.getAuthor_name());
            likes.setThumbnail_pic_s(news.getThumbnail_pic_s());
            likesMapper.insert(likes);
        } else if (type == -1) { // 2 取消点赞行为
            // 2.1 更新news表中对应点赞数的信息
            QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
            newsQueryWrapper.eq("url", url);
            News news = newsMapper.selectOne(newsQueryWrapper);
            if (news == null) {
                return false;
            }
            news.setLikes(news.getLikes() - 1);
            UpdateWrapper<News> newsUpdateWrapper = new UpdateWrapper<>();
            newsUpdateWrapper.eq("url", url);
            newsMapper.update(news, newsUpdateWrapper);

            // 2.2 删除likes表中对应的一条信息
            QueryWrapper<Likes> likesWrapper = new QueryWrapper<>();
            likesWrapper.eq("openid", openid);
            likesWrapper.eq("url", url);
            likesMapper.delete(likesWrapper);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<contentNewsList> getLikes(String openid) {
        // 1 查找所有该用户的点赞信息
        QueryWrapper<Likes> likesQueryWrapper = new QueryWrapper<>();
        likesQueryWrapper.eq("openid", openid);
        List<Likes> likes = likesMapper.selectList(likesQueryWrapper);

        // 2 写入包装类
        List<contentNewsList> contentNewsLists = new ArrayList<>();
        for (Likes like : likes) {
            // 2.1 查找这个点赞新闻对应的日期
            QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
            newsQueryWrapper.eq("url", like.getUrl());
            String date = newsMapper.selectOne(newsQueryWrapper).getDate();

            // 2.2 插入相关信息到链表中
            contentNewsList c = new contentNewsList();
            c.setUrl(like.getUrl());
            c.setTitle(like.getTitle());
            c.setDate(date);
            c.setThumbnail_pic_s(like.getThumbnail_pic_s());
            c.setAuthor_name(like.getAuthor_name());
            contentNewsLists.add(c);
        }

        // 3 返回查询结果
        return contentNewsLists;
    }

    @Override
    public boolean ifLiked(String url, String openid) {
        QueryWrapper<Likes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",url);//https://mini.eastday.com/mobile/230613145926066985548.html
        queryWrapper.eq("openid",openid);//ocWC66WG7HA18075IAUdxjWXNjww
        List<Likes> likesList = likesMapper.selectList(queryWrapper);
        return  !likesList.isEmpty();
    }
}
