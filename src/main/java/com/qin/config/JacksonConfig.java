package com.qin.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author qinhanhan
 */
@JsonComponent
public class JacksonConfig {
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN)));
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDateTime.parse(p.getText(), DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN));
        }
    }

    public static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        }
    }

    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDate.parse(p.getText(), DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        }
    }

    public static class LocalTimeSerializer extends JsonSerializer<LocalTime> {
        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        }
    }

    public static class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//            p.getCodec().readTree(p).get("fieldName");
            return LocalTime.parse(p.getText(), DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
        }
    }

    /**
     * Jackson 序列化 long类型为String，解决后端返回的Long类型在前端精度丢失的问题
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    @Order(1)
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance)
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(BigInteger.class, ToStringSerializer.instance);
    }

}
