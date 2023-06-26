package com.qin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.qin.config.JacksonConfig;
import com.qin.config.JacksonNullSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 在 springboot 加载完成后执行一些东西
 * @author qinhanhan
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    ObjectMapper objectMapper;
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    JacksonNullSerializer jacksonNullSerializer;
    @Autowired
    public void setJacksonNullSerializer(JacksonNullSerializer jacksonNullSerializer) {
       this.jacksonNullSerializer = jacksonNullSerializer;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 修改jackson 对空值的处理
        objectMapper.getSerializerProvider().setNullValueSerializer(jacksonNullSerializer);
    }
}
