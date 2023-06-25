package com.qin.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

@Log4j2
@Component
public class JacksonNullSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Object currentValue = gen.getOutputContext().getCurrentValue();
        Class<?> aClass = currentValue.getClass();
        String currentName = gen.getOutputContext().getCurrentName();
        Field[] fields = aClass.getDeclaredFields();
        Class<?> valueClass = null;
        int i = 0, length = fields.length;
        while (i < length) {
            Field field = fields[i];
            if (currentName.equals(field.getName())) {
                valueClass = field.getType();
                break;
            }
            i++;
            if (i >= length) {
                aClass = aClass.getSuperclass();
                fields = aClass.getDeclaredFields();
                i = 0;
                length = fields.length;
            }
        }
        if (valueClass != null) {
            if (isStringType(valueClass)) {
                gen.writeString("");
            } else if (isNumberType(valueClass)) {
                gen.writeNumber(0);
            } else if (isLocalDateTimeType(valueClass)) {
                gen.writeString("0000-00-00 00:00:00");
            } else if (isBooleanType(valueClass)) {
                gen.writeBoolean(false);
            } else if (isArrayType(valueClass)) {
                gen.writeStartArray();
                gen.writeEndArray();
            } else {
                gen.writeString("{}");
            }
        } else {
            gen.writeNull();
        }
    }

    /**
     * 是否是string 或者是 字符包装类Character 的子类 其他可变字符类 StringBuilder 等
     */
    private boolean isStringType(Class<?> valueClass) {
        return valueClass.equals(String.class) || Character.class.isAssignableFrom(valueClass) || CharSequence.class.isAssignableFrom(valueClass);
    }
    /**
     * 是否是 number 类型
     */
    private boolean isNumberType(Class<?> valueClass) {
        return Number.class.isAssignableFrom(valueClass);
    }
    /**
     * 是否是boolean
     */
    private boolean isBooleanType(Class<?> valueClass) {
        return valueClass.equals(Boolean.class);
    }
    /**
     * 是否是LocalDateTime
     */
    private boolean isLocalDateTimeType(Class<?> valueClass) {
        return valueClass.equals(LocalDateTime.class);
    }
    /**
     * 是否是数组
     */
    private boolean isArrayType(Class<?> valueClass) {
        return valueClass.isArray() || Collection.class.isAssignableFrom(valueClass);
    }
    private boolean isObjectType(Class<?> valueClass) {
        return Object.class.isAssignableFrom(valueClass);
    }
    /**
     * 是否是LocalTime
     */
    private boolean isLocalTime(Class<?> valueClass) {
        return valueClass.equals(LocalTime.class);
    }
    /**
     * 是否是LocalDate
     */
    private boolean isLocalDate(Class<?> valueClass) {
        return valueClass.equals(LocalDate.class);
    }
}
