package com.example.demo.service;

import com.example.demo.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xzx
 * @since 2023-06-13
 */
public interface CommentService extends IService<Comment> {
    // 添加一条新的评论
    boolean addComment(String openid, String content, String url);
}
