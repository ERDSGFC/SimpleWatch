package com.qin.simplewatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.pojo.po.UserPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SpringBootTest
public class RedisTest {
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;
    @Test
    void redisObjectTest() throws JsonProcessingException {
        UserPO user = new UserPO();
        user.setId(1);
        user.setUserName("小黑");
        user.setUserPassword("123456");
        user.setStatus((byte) 2);
        user.setLoginState((byte) 0);
        user.setType((byte) 1);
        user.setLoginCount(2222);
        user.setJoinTime(LocalDateTime.of(1997, 1, 1, 11, 4, 55));
        user.setLoginTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        user.setDeletedTime(LocalDateTime.now());

        redisTemplate.opsForValue().set("temp_test", user);
        System.out.println(redisTemplate.opsForValue().get("temp_test"));
        System.out.println(stringRedisTemplate.opsForValue().get("temp_test"));
        stringRedisTemplate.opsForValue().set("temp_test_string", objectMapper.writeValueAsString(user));
        System.out.println(redisTemplate.opsForValue().get("temp_test"));
        redisTemplate.opsForHash().put("map", "mapKey", user);
        HashMap<String, Object> map = new HashMap<>();
//        map.put("id", 1333L);
//        map.put("name", "名称");
//        map.put("status", (byte)1);
//        map.put("sort", 1);
//        map.put("num", -1);
//        map.put("array", new int[0]);
//        map.put("arrayNull", null);
//        map.put("array1", new int[1]);
//        map.put("array12", new int[]{1});
        map.put("arrayList", new ArrayList<Long>());
        map.put("arrayListStr", new ArrayList<String>());
        map.put("arrayListNO", Arrays.asList(1L,2L,222222L));
        map.put("arrayListStrNO", Arrays.asList("wo", "tt", "heiehi"));
        map.put("arrayListIntNO", Arrays.asList(1,11,111,111));
        redisTemplate.opsForHash().putAll("map_map", map);
    }


    @Test
    public void redisTest() {
        var a = 1;
       stringRedisTemplate.opsForValue().set("test", "我的");
        String test = stringRedisTemplate.opsForValue().get("test");
        System.out.println(test);
        stringRedisTemplate.opsForValue().set("testnumber", "12");
        String num = stringRedisTemplate.opsForValue().get("testnumber");
        System.out.println(num);
        stringRedisTemplate.opsForList().leftPush("list","left_push");
        String list = stringRedisTemplate.opsForList().index("list", 0);
        System.out.println(list);
//        stringRedisTemplate.boundHashOps("test").pua
//        String list = stringRedisTemplate.opsForList().index("list", 0);
//        System.out.println(list);
    }
}
