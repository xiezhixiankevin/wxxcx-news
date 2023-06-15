package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.service.LikesService;
import com.example.demo.utils.JwtUtil;
import com.example.demo.utils.contentNewsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikesController {
    @Autowired
    private LikesService likesService;
    @Autowired
    private JwtUtil jwtUtil;

    // 点赞接口
    @PostMapping("/ifLiked")
    public R<?> ifLiked(@RequestParam String url, HttpServletRequest request) {
        // 1 从token中获取openid
        String token = request.getHeader("Authorization");
        String openid = jwtUtil.getOpenidFromToken(token);

        // 2 处理点赞行为
        boolean ifLiked =likesService.ifLiked(url,openid);

        return R.ok(ifLiked);
    }

    // 点赞接口
    @PostMapping("/kudos")
    public R<?> kudos(@RequestParam String url, @RequestParam int type, HttpServletRequest request) {
        // 1 从token中获取openid
        String token = request.getHeader("Authorization");
        String openid = jwtUtil.getOpenidFromToken(token);

        // 2 处理点赞行为
        boolean success = likesService.kudos(openid, url, type);

        // 3 返回操作是否成功的信息
        if (success) {
            return R.failed("点赞成功");
        } else {
            return R.ok(null);
        }
    }

    // 获取对应用户所有的点赞新闻信息
    @GetMapping("/getLikes")
    public R<?> getLikes(HttpServletRequest request) {
        // 1 从token中获取openid
        String token = request.getHeader("Authorization");
        String openid = jwtUtil.getOpenidFromToken(token);

        // 2 获取所有的点赞信息
        List<contentNewsList> likes = likesService.getLikes(openid);

        // 3 返回结果
        return R.ok(likes);
    }
}
