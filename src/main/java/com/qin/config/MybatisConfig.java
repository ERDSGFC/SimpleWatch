package com.qin.config;

import com.qin.common.MybatisIntercept;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

//    public void typeHandler(SqlSessionFactory sqlSessionFactory) {
//
//    }
    @Bean
    public String MyBatisInterceptorSetting(SqlSessionFactory sqlSessionFactory) {
        MybatisIntercept mybatisIntercept = new MybatisIntercept();
        // 给拦截器添加自定义参数
        sqlSessionFactory.getConfiguration().addInterceptor(mybatisIntercept);
//        sqlSessionFactory.getConfiguration().han
        return "interceptor";
    }

}
