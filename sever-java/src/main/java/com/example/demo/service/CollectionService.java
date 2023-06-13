package com.example.demo.service;

import com.example.demo.pojo.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.utils.contentNewsList;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xzx
 * @since 2023-06-13
 */
public interface CollectionService extends IService<Collection> {
    // 插入新的数据
    boolean addCollection(Collection collection);

    // 处理收藏功能
    boolean collect(String openid, String url, int type);

    // 获取对应用户的所有收藏新闻信息
    List<contentNewsList> getCollections(String openid);

    boolean ifCollected(String url, String openid);
}
