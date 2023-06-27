package com.qin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Log4j2
public class JsonUtils {
    private JsonUtils() {}
    private static ObjectMapper mapper;

    /**
     * 只在初始化的时候设置一次
     */
    public static void setMapper(final ObjectMapper objectMapper) {
        if (Objects.isNull(mapper)) JsonUtils.mapper = objectMapper;
    }

    /**
     * 序列化，将对象转化为json字符串
     *
     * @param data  对象
     * @return json 返回
     */
    public static String toJsonString(@NonNull final Object data) {
        String json = null;
        try {
            json = JsonUtils.mapper.writeValueAsString(data);
        } catch (final JsonProcessingException e) {
            JsonUtils.log.error("[{}] toJsonString error：{{}}", data.getClass().getSimpleName(), e);
        }
        return json;
    }

    /**
     * 反序列化，将json字符串转化为对象
     *
     * @param json json 字符串
     * @param clazz 类的.class
     * @return 返回对象
     */
    public static <T> T parse(@NonNull final String json, final Class<T> clazz) {
        T t = null;
        try {
            t = JsonUtils.mapper.readValue(json, clazz);
        } catch (final Exception e) {
            JsonUtils.log.error(" parse json [{}] to class [{}] error：{{}}", json, clazz.getSimpleName(), e);
        }
        return t;
    }
}
