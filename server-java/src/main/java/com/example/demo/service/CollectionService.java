package com.example.demo.service;

import com.example.demo.pojo.Collection;
import com.baomidou.mybatisplus.extension.service.IService;

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
    public boolean addCollection(Collection collection);
}
