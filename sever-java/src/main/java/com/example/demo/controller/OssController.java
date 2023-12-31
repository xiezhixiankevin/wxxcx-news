package com.example.demo.controller;


import com.example.demo.config.interceptor.NoNeedAuthorization;
import com.example.demo.service.IOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss")
@NoNeedAuthorization
public class OssController {
    @Autowired
    private IOssService ossService;



    /**
     * 上传文件
     * @param file
     * @return url
     */
    @PostMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file)  {
        return ossService.upload(file);
    }
}
