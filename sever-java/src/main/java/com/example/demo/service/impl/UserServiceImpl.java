package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public User queryUserInfo(String openid) {
//        return userMapper.getUserByOpenid(openid);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);

        List<User> users = userMapper.selectList(wrapper);

        if (users.isEmpty())return null;
        else {
            User user = users.get(0);

            return user;
        }
    }

    @Override
    public int addUser(String openid) {
        User user = new User();
        user.setOpenid(openid);
        user.setNickname("我想改名");
        user.setDescription("我想改个性签名");
        user.setAvatarUrl("https://huangzelin.oss-cn-beijing.aliyuncs.com/dongji/images/2023-03-31f41dac0bc4d44068b9618f316dbb65085a60e1aee2489001a8c53d956f71c61.jpg");

        return userMapper.insert(user);
    }

    @Override
    public int uploadAvatar(String openid, String avatarUrl) {

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("openid",openid);
        User user = new User();
        user.setOpenid(openid);
        user.setAvatarUrl(avatarUrl);

        return userMapper.update(user,updateWrapper);
    }


    @Override
    public R getUserInfo(String openid) {

        return R.ok("hai meixie");
    }


    @Override
    public R saveUserInfo(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        String openid = user.getOpenid();
        updateWrapper.eq("openid",openid);
        user.setBirthday("2001-11-11");
        int isOk = userMapper.update(user, updateWrapper);
        if(isOk==0){
            log.error("基本信息存入失败");
            return R.failed("基本信息存入失败");
        }
        return R.ok("true");

    }

}
