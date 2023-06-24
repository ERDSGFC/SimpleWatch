package com.qin;

import com.qin.utils.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.TimeZone;

/**
 * @author qinhanhan
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableCaching
@MapperScan("com.qin.dao")
public class Application {

    public static void main(String[] args) {
        // 设置时区 保证 时间的一致性
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai")); //America/New_York
        SpringApplication application = new SpringApplication(Application.class);
        ConfigurableApplicationContext context = application.run(args);
        SpringContextUtil.setContext(context);
    }

}
