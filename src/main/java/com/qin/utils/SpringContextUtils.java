package com.qin.utils;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

public class SpringContextUtils {

    private static ConfigurableApplicationContext context;

    private SpringContextUtils(){}

    /**
     * 设置 spring boot 容器
     * 只在初始化的时候设置一次
     * @param run
     */
    public static void setContext(final ConfigurableApplicationContext run) {
        if (Objects.isNull(run)) SpringContextUtils.context = run;
    }

    /**
     * 获取spring boot 容器
     * @return the context
     */
    public static ConfigurableApplicationContext getContext() {
        return SpringContextUtils.context;
    }

    /**
     * @param <T> Bean.class
     * @param beanType Bean 的类型
     * @return
     */
    public <T> T getBean(final Class<T> beanType) {
       return SpringContextUtils.context.getBean(beanType);
    }

    /**
     * @param <T>
     * @param name
     * @return
     */
    public <T> T getBean(final String name, final Class<T> beanType) {
       return SpringContextUtils.context.getBean(name, beanType);
    }

}
