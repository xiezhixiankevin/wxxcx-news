package com.example.demo.service.impl;

import com.example.demo.pojo.Comment;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
