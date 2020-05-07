package com.qyh.demo.base.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * Created with IDEA
 * author:huxi
 * Date:2019/4/19
 * Time:10:47 AM
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean streamFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new StreamFilter());
        registration.addUrlPatterns("/*");
        registration.setName("streamFilter");
        registration.setOrder(3);
        return registration;
    }
}
