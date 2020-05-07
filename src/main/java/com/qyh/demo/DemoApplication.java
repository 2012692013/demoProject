package com.qyh.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@ServletComponentScan // 扫描使用注解方式的servlet
@EnableScheduling//启用定时任务
@SpringBootApplication(scanBasePackages = {"com.qyh"})
@MapperScan("com.qyh.demo.mapper")
@Slf4j
public class DemoApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);

    }

}