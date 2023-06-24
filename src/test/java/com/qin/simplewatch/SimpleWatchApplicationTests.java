package com.qin.simplewatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.dao.UserMapper;
import com.qin.pojo.po.UserPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;

@SpringBootTest
class SimpleWatchApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void mysqlTest() {
        System.out.println(this.userMapper.findOneById(1));
        System.out.println("run time");
    }

    @Autowired
    ObjectMapper objectMapper;
    @Test
    void jsonTset() throws JsonProcessingException {

        final UserPO user = new UserPO();
        user.setId(123456788);
        user.setDeletedTime(LocalDateTime.now());

        System.out.println(this.objectMapper.writeValueAsString(user));
    }

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisTemplate redisTemplate;

    @Test
    void redisTest() {
        final StringRedisTemplate re = new StringRedisTemplate();

        this.stringRedisTemplate.opsForValue().set("11", "33");
        this.redisTemplate.opsForValue().set("test", new UserPO());

    }

}
