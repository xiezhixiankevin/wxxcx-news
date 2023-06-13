package com.example.demo.controller;



import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.config.interceptor.NoNeedAuthorization;
import com.example.demo.pojo.User;
import com.example.demo.service.IUserService;
import com.example.demo.utils.EncryptUtil;
import com.example.demo.utils.GetUserInfoUtil;
import com.example.demo.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Autowired
    private OssController ossController;


    @Autowired
    JwtUtil jwtUtil;

//    @ApiOperation("微信授权登录")

    /**
     * 微信授权登录
     * @param code
     * @return
     */
    @PostMapping("/login")
    @NoNeedAuthorization
    public R authorizeLogin(@RequestParam("code") String code) {

        //通过code换取信息
        JSONObject resultJson = GetUserInfoUtil.getResultJson(code);

        if (resultJson.has("openid")){
            //获取sessionKey和openId
            String sessionKey = resultJson.get("session_key").toString();
            String openid = resultJson.get("openid").toString();
            log.info("openid："+openid);

            //生成自定义登录态session
            String session = null;
            Map<String,String> sessionMap = new HashMap<>();

            sessionMap.put("sessionKey",sessionKey);
            sessionMap.put("openid",openid);
            session = JSONObject.fromObject(sessionMap).toString();

            //加密session
            try {
                EncryptUtil encryptUtil = new EncryptUtil();
                session = encryptUtil.encrypt(session);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //生成token
            String token = jwtUtil.getToken(session);
            Map<String,String> result = new HashMap<>();
            result.put("token", token);

            //查询用户是否存在
            User user = userService.queryUserInfo(openid);
            if (user != null){

                return R.ok(result); //用户存在，返回结果
            }else { //用户不存在，新建用户信息
                int rs = userService.addUser(openid);
                if (rs <= 0){
                    return R.failed("登录失败");
                }
                return R.ok(result);
            }
        }

        return R.failed("授权失败"+ resultJson.getString("errmsg"));
    }

    /**
     * 存储用户头像
     * @param file
     * @param request
     * @return
     */
    //TODO 未测试
    @PostMapping("/uploadAvatar")
    public R uploadAvatar(@RequestParam("file") MultipartFile file,
                                   HttpServletRequest request){

        //将file存到阿里云oss中,获得url
        log.info("去上传照片");
        String avatarUrl = ossController.upload(file);
        log.info("上传成功："+avatarUrl);
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);

        int result = userService.uploadAvatar(openid, avatarUrl);
        if(result <= 0){
            return R.failed("更新失败！");
        }

        return R.ok(" ");
    }


    /**
     * 保存用户基本信息
     * @param object
     * @param request
     * @return
     */
    @PostMapping("/saveUserInfo")
    public R saveUserInfo(@RequestBody cn.hutool.json.JSONObject object, HttpServletRequest request){
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);

        System.out.println(object);

        User user = JSONUtil.toBean(object, User.class);

        user.setOpenid(openid);

        return userService.saveUserInfo(user);
    }





    /**
     * 获取用户基本信息
     * @param request
     * @return
     */
    @GetMapping("/getUserInfo")
    public R getUserInfo(HttpServletRequest request){
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);

        User user = userService.queryUserInfo(openid);

//        return userService.getUserInfo(openid);
        return R.ok(user);
    }




}
