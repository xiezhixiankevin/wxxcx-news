package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.pojo.News;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsMapper extends BaseMapper<News> {
}
