package com.qin.simplewatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.common.dao.update.UpdateMapper;
import com.qin.dao.UserMapper;
import com.qin.pojo.po.UserPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

@SpringBootTest
public class Mybatis {

    @Autowired
    UserMapper userMapper;
    @Test
    void redisObjectTest() throws JsonProcessingException {
        UserPO user = new UserPO();
        user.setId(5);
        user.setUserName("test1");
        user.setUserPassword("test1");
//        userMapper.insertObj(user, "user", "id");
//        userMapper.insertObj(user);
        UserPO userPO = userMapper.findOneById(8);

        UserPO newUser = new UserPO();
        newUser.setId(userPO.getId());
        newUser.setUserName(userPO.getUserName());
        newUser.setUserPassword("ttt");
        newUser.setStatus(userPO.getStatus());
        newUser.setLoginState(user.getLoginState());
        newUser.setType(userPO.getType());
        newUser.setLoginCount(userPO.getLoginCount());
        newUser.setJoinTime(userPO.getJoinTime());
        newUser.setLoginTime(userPO.getLoginTime());
        newUser.setUpdatedTime(LocalDateTime.now());
        newUser.setDeletedTime(LocalDateTime.now());


//        Objects.c
        userMapper.updateChangeFieldsById(newUser, userPO, 8);

    }
}
