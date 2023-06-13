package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.pojo.Comment;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xzx
 * @since 2023-06-13
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/listComments")
    public R listComments(@RequestParam String url){
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("url", url);
        List<Comment> list = commentService.list(commentQueryWrapper);
        return R.ok(list);
    }

    @PostMapping("/addComments")
    public R addComments(Comment comment){
        commentService.save(comment);
        return R.ok(null);
    }

}

