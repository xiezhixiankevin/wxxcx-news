package com.example.demo.service.impl;

import com.example.demo.pojo.Collection;
import com.example.demo.mapper.CollectionMapper;
import com.example.demo.service.CollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public boolean addCollection(Collection collection) {
        return collectionMapper.insert(collection) == 1;
    }
}
