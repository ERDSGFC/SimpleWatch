package com.qin.simplewatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.dao.UserMapper;
import com.qin.pojo.po.UserPO;
import lombok.Builder;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;

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
//        objectMapper.getS;
//        objectMapper.getSerializerFactory().withSerializerModifier();
//        JsonTest test = JsonTest.builder().id(1).a(new int[]{1}).build();
        var test = new JsonTest();
        test.setTime(LocalDateTime.now());
//        test.setMap(new HashMap<>());
        System.out.println(test.toString());

        System.out.println(this.objectMapper.writeValueAsString(test));
    }
}

