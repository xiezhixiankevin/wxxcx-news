package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface IOssService {

    String upload(MultipartFile file);

}
