package com.lixuan.smart_lock.controller;


import com.lixuan.smart_lock.domain.UserEntity;
import com.lixuan.smart_lock.repository.UserRepository;


import com.lixuan.smart_lock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    //用户注册

    @PostMapping(value = "users/registration")
    public String userRegist(UserEntity userEntity) {

        userEntity.setIdentity(userEntity.getIdentity());
        userEntity.setPassword(userEntity.getPassword());
        userEntity.setUsername(userEntity.getUsername());
        userEntity.setApplyProxy(false);
        userEntity.setApplyRent(false);
        userEntity.setProxy(false);
        userEntity.setRent(false);

        userRepository.save(userEntity);
        return "用户注册成功";
    }

    //用户登陆

    @PostMapping(value = "users/log")
    public List<ArrayList<String>> log(UserEntity userEntity) {

        List<ArrayList<String>> userList = new ArrayList<>();
        userList = userService.userLog(userEntity);
        return userList;

    }

    //租客申请租房
    @PostMapping(value = "renter/authorization/applyForRent")
    public String applyRent(@RequestParam("username") String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (!user.isApplyRent() && !user.isRent()) {
            user.setApplyRent(true);
            userRepository.save(user);
            return "正在申请租房";
        }

        else if (user.isApplyRent() && !user.isRent()) {
            return "租客已经在租房";
        }

        else if (!user.isApplyRent() && user.isRent()) {
            return "租客已经租到房";
        }

        return "";


    }

    //租客申请代理
    @PostMapping(value = "renter/authorization/applyForProxy")
    public String applyProxy(@RequestParam("username") String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (!user.isProxy() && !user.isApplyProxy()) {
            user.setApplyProxy(true);
            userRepository.save(user);
            return "正在申请代理";
        }

        else if (user.isApplyProxy() && !user.isProxy()) {
            return "租客已经在申请代理";
        }

        else if (!user.isApplyProxy() && user.isProxy()) {
            return "租客已经得到代理权限";
        }

        else return "";


    }

    //房主给代理权限
    @PostMapping(value = "owner/users/authorization/proxy")
    public String ownerAllowProxy(@RequestParam("username") String username) {

        UserEntity user = userRepository.findByUsername(username);
        user.setProxy(true);
        user.setApplyProxy(false);
        user.setIdentity("proxy");
        userRepository.save(user);
        return "成功授予" + user.getUsername() + "代理权限";

    }

    //房主给租客租住权限
    @PostMapping(value = "owner/users/authorization/rent")
    public String ownerAllowRent(@RequestParam("username") String username) {
        UserEntity user = userRepository.findByUsername(username);
        user.setRent(true);
        user.setApplyRent(false);
        userRepository.save(user);
        return "成功授予" + user.getUsername() + "租住权限";
    }

    //房主撤销代理者代理权限
    @PostMapping(value = "owner/users/cancel/proxy")
    public String ownerCancelProxy(@RequestParam("username") String username) {
        UserEntity user = userRepository.findByUsername(username);
        user.setProxy(false);
        user.setIdentity("renter");
        userRepository.save(user);
        return "已撤销" + user.getUsername() +"的代理权限";
    }

    //房主撤销租客租住权限
    @PostMapping(value = "owner/users/cancel/rent")
    public String ownerCancelRent(@RequestParam("username") String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user.isRent()) user.setRent(false);
        userRepository.save(user);
        return "已撤销" + user.getUsername() + "的租住权限";
    }

    //代理给租客租住权限
    @PostMapping(value = "proxy/users/authorization/rent")
    public String proxyAllowRent(@RequestParam("username") String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (!user.isRent()) {
            user.setRent(true);
            user.setApplyRent(false);
            userRepository.save(user);
        }
        return "已授予" + user.getUsername() + "租住权限";
    }

    //代理撤销租客租住权限
    @PostMapping(value = "proxy/users/cancel/rent")
    public String proxyCancelRent(@RequestParam("username") String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user.isRent()) user.setRent(false);
        userRepository.save(user);
        return "已撤销" + user.getUsername() + "的租住权限";
    }




}
