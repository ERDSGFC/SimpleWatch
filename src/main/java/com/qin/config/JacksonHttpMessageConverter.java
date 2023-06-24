package com.qin.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("all")
public class JacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    /**
     * 处理数组类型的null值
     */
    public static class NullArrayJsonSerializer extends JsonSerializer<Object> {


        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeStartArray();
                gen.writeEndArray();
            }
        }
    }


    /**
     * 处理字符串等类型的null值
     */
    public static class  NullStringJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString("");
        }
    }

    /**
     * 处理字符串等类型的null值
     */
    public static class NullNumberJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeNumber(0);
        }
    }

    /**
     * 处理字符串等类型的null值
     */
    public static class NullBooleanJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeBoolean(false);
        }
    }

    public static class NullLocalDateTimeJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("");
        }
    }

    public static class NullObjectJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeStartObject();
                gen.writeEndObject();
            }
        }
    }


    public static class MyBeanSerializerModifier extends BeanSerializerModifier {


        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            //循环所有的beanPropertyWriter
            for (Object beanProperty : beanProperties) {
                BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
                //判断字段的类型，如果是array，list，set则注册nullSerializer
                if (isArrayType(writer)) {
                    //给writer注册一个自己的nullSerializer
                    writer.assignNullSerializer(new NullArrayJsonSerializer());
                } else if (isNumberType(writer)) {
                    writer.assignNullSerializer(new NullNumberJsonSerializer());
                } else if (isBooleanType(writer)) {
                    writer.assignNullSerializer(new NullBooleanJsonSerializer());
                } else if (isStringType(writer)) {
                    writer.assignNullSerializer(new NullStringJsonSerializer());
                }
//                else if (isLocalDateTimeType(writer)) {
//                    writer.assignNullSerializer(new NullLocalDateTimeJsonSerializer());
//                }
//                else if(isObjectType(writer)){
//                    writer.assignNullSerializer(new NullObjectJsonSerializer());
//                }
            }
            return beanProperties;
        }

        private boolean isObjectType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return Object.class.isAssignableFrom(clazz);
        }

        private boolean isLocalDateTimeType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return LocalDateTime.class.isAssignableFrom(clazz) || LocalDateTime.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是数组
         */
        private boolean isArrayType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是string
         */
        private boolean isStringType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
        }


        /**
         * 是否是int
         */
        private boolean isNumberType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return Number.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是boolean
         */
        private boolean isBooleanType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.equals(Boolean.class);
        }

    }

    JacksonHttpMessageConverter() {
        getObjectMapper().setSerializerFactory(getObjectMapper().getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
    }
}
