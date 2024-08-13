package com.inspur.dsp.direct.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by jamesli on 2016/3/21.
 *
 * @author james li
 * @version 1.0
 * @date 2016-4-20
 * @SpringBootApplication等价于下面标签： 1. @Configuration
 * 2. @EnableAutoConfiguration
 * 3. @EnableWebMvc
 * 4. @ComponentScan
 * @ServletComponentScan 1. @WebListener
 * 2. @WebFilter
 * 3. @WebSevlet
 */
@EnableAsync
@SpringBootApplication
@ServletComponentScan
@ComponentScan(basePackages = "com.inspur",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "com.inspur.dsp.common.db.config.MybatisConfig")
    }
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}