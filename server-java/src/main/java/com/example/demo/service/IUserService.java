package com.example.demo.service;


import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.pojo.User;

public interface IUserService {

    /**
     * 通过openid查询用户
     * @param openid
     * @return
     */
    User queryUserInfo(String openid);

    /**
     * 添加用户
     * @param openid
     * @return
     */
    int addUser(String openid);


    /**
     * 获取用户基本信息
     * @param openid
     * @return
     */
    R getUserInfo(String openid);


    /**
     * 保存用户基本信息
     * @param user
     * @return
     */
    R saveUserInfo(User user);


    /**
     * 更新用户头像链接
     *
     * @param openid
     * @param avatarUrl
     * @return
     */

    int uploadAvatar(String openid, String avatarUrl);
}
