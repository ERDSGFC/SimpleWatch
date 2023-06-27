package com.qin.common.dao;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 是否忽略
 */
@Retention(RUNTIME)
@Target({ FIELD })
public @interface IgnoreFiled {
    /**
     * 是否强制忽略
     */
    boolean value() default false;

}
