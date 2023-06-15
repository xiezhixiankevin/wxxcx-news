package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.pojo.Collection;
import com.example.demo.mapper.CollectionMapper;
import com.example.demo.pojo.News;
import com.example.demo.service.CollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.contentNewsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xzx
 * @since 2023-06-13
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {
    @Autowired
    private CollectionMapper collectionMapper;
    @Autowired
    private NewsMapper newsMapper;
    @Override
    public boolean addCollection(Collection collection) {
        return collectionMapper.insert(collection) == 1;
    }

    @Override
    public boolean collect(String openid, String url, int type) {
        // 1 点赞行为
        if (type == 1) {
            // 1.1 更新news表中对应收藏数的信息
            QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
            newsQueryWrapper.eq("url", url);
            News news = newsMapper.selectOne(newsQueryWrapper);
            if (news == null) {
                return false;
            }
            news.setCollections(news.getCollections() + 1);
            UpdateWrapper<News> newsUpdateWrapper = new UpdateWrapper<>();
            newsUpdateWrapper.eq("url", url);
            newsMapper.update(news, newsUpdateWrapper);

            // 1.2 新插入collection表一条信息
            Collection collection = new Collection();
            collection.setAuthor_name(news.getAuthor_name());
            collection.setUrl(url);
            collection.setTitle(news.getTitle());
            collection.setOpenid(openid);
            collection.setThumbnail_pic_s(news.getThumbnail_pic_s());
            collectionMapper.insert(collection);
        } else if (type == -1) { // 2 取消收藏行为
            // 2.1 更新news表中对应收藏数的信息
            QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
            newsQueryWrapper.eq("url", url);
            News news = newsMapper.selectOne(newsQueryWrapper);
            if (news == null) {
                return false;
            }
            news.setCollections(news.getCollections() -1);
            UpdateWrapper<News> newsUpdateWrapper = new UpdateWrapper<>();
            newsUpdateWrapper.eq("url", url);
            newsMapper.update(news, newsUpdateWrapper);

            // 2.2 删除collection表中对应的一条信息
            QueryWrapper<Collection> collectionQueryWrapper = new QueryWrapper<>();
            collectionQueryWrapper.eq("openid", openid);
            collectionQueryWrapper.eq("url", url);
            collectionMapper.delete(collectionQueryWrapper);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<contentNewsList> getCollections(String openid) {
        // 1 查找所有该用户的收藏信息
        QueryWrapper<Collection> collectionQueryWrapper = new QueryWrapper<>();
        collectionQueryWrapper.eq("openid", openid);
        List<Collection> collections = collectionMapper.selectList(collectionQueryWrapper);

        // 2 写入包装类
        List<contentNewsList> contentNewsLists = new ArrayList<>();
        for (Collection collection : collections) {
            // 2.1 查找这个收藏新闻对应的日期
            QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
            newsQueryWrapper.eq("url", collection.getUrl());
            String date = newsMapper.selectOne(newsQueryWrapper).getDate();

            // 2.2 插入相关信息到链表中
            contentNewsList c = new contentNewsList();
            c.setUrl(collection.getUrl());
            c.setTitle(collection.getTitle());
            c.setDate(date);
            c.setThumbnail_pic_s(collection.getThumbnail_pic_s());
            c.setAuthor_name(collection.getAuthor_name());
            contentNewsLists.add(c);
        }

        // 3 返回查询结果
        return contentNewsLists;
    }

    @Override
    public boolean ifCollected(String url, String openid) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",url);
        queryWrapper.eq("openid",openid);
        return collectionMapper.selectOne(queryWrapper) != null;
    }
}
