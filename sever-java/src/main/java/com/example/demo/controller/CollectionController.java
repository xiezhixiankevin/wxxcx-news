package com.example.demo.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.pojo.Collection;
import com.example.demo.service.CollectionService;
import com.example.demo.utils.JwtUtil;
import com.example.demo.utils.contentNewsList;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/collection")
@Slf4j
public class CollectionController {
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/addCollection")
    public R<?> addCollection(@RequestBody Collection collection) {
        if (collectionService.addCollection(collection)) {
            return R.ok(null);
        } else {
            return R.failed("插入失败");
        }
    }

    // 收藏接口
    @PostMapping("/collect")
    public R<?> collect(@RequestParam String url, @RequestParam int type, HttpServletRequest request) {
        // 1 从token中获取openid
        String token = request.getHeader("Authorization");
        String openid = jwtUtil.getOpenidFromToken(token);

        // 2 处理收藏行为
        boolean success = collectionService.collect(openid, url, type);

        // 3 返回操作是否成功的信息
        if (success) {
            return R.failed("收藏成功");
        } else {
            return R.ok(null);
        }
    }

    // 获取对应用户所有的收藏新闻信息
    @GetMapping("/getCollections")
    public R<?> getCollections(HttpServletRequest request) {
        // 1 从token中获取openid
        String token = request.getHeader("Authorization");
        String openid = jwtUtil.getOpenidFromToken(token);
        log.info("正在调用getCollections接口");

        // 2 获取所有的收藏信息
        List<contentNewsList> collections = collectionService.getCollections(openid);

        // 3 返回结果
        return R.ok(collections);
    }

    // 判断是否收藏
    @PostMapping("/ifCollected")
    public R<?> ifLiked(@RequestParam String url, HttpServletRequest request) {
        // 1 从token中获取openid
        String token = request.getHeader("Authorization");
        String openid = jwtUtil.getOpenidFromToken(token);

        // 2 判断是否收藏
        boolean ifCollected =collectionService.ifCollected(url,openid);

        return R.ok(ifCollected);
    }
}

