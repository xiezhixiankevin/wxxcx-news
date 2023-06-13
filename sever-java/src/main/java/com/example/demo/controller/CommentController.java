package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.pojo.Comment;
import com.example.demo.service.CommentService;
import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("/addNewComment")
    public R<?> addNewComment(@RequestParam String content,
                              @RequestParam String url,
                              HttpServletRequest request) {
        // 1 从token中获取openid
        String token = request.getHeader("Authorization");
        String openid = jwtUtil.getOpenidFromToken(token);

        // 2 向数据库中添加新的评论信息
        boolean success = commentService.addComment(openid, content, url);

        // 3 返回操作是否成功的信息
        if (success) {
            return R.ok(null);
        } else {
            return R.failed("添加失败");

        }
    }
}

