package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.Comment;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.CommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean addComment(String openid, String content, String url) {
        // 1 通过openid获取该用户的信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("openid", openid);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            return false;
        }
        // 2 将评论需要的内容插入数据库
        Comment commentEntity = new Comment();
        commentEntity.setOpenid(openid);
        commentEntity.setContent(content);
        commentEntity.setUrl(url);
        commentEntity.setNickname(user.getNickname());
        commentEntity.setAvatarUrl(user.getAvatarUrl());
        commentMapper.insert(commentEntity);
        return true;
    }
}
