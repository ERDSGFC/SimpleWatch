package com.qin.controller;

import com.qin.dao.UserMapper;
import com.qin.pojo.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("base")
//    @Transactional
    public String baseTest() {
        UserPO user = new UserPO();
        user.setUserName("heiheiss");
        user.setUserPassword("heihei");
        boolean flag = userMapper.insertObj(user);
//        userMapper.findOneById(1);
//        System.out.println("this is a f");
//        userMapper.findOneById(1);
//        System.out.println("=======================================");
//        userMapper.findOneById(2);
        return "this is a test ttt sf";
    }
}
