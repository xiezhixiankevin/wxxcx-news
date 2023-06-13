package com.example.demo.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.pojo.Collection;
import com.example.demo.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @PostMapping("/addCollection")
    public R<?> addCollection(@RequestBody Collection collection) {
        if (collectionService.addCollection(collection)) {
            return R.ok(null);
        } else {
            return R.failed("插入失败");
        }
    }
}

