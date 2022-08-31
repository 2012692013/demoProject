package com.qyh.demo.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.qyh.demo.base.interceptor.LoginInterceptor;

/**
 * 注册拦截器
 * @author qiuyuehao
 * 2018年6月12日
 */

@Configuration
public class LoginInterceptorConfig extends WebMvcConfigurerAdapter{

	
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(new LoginInterceptor());
        System.out.println("===========   拦截器注册完毕   ===========");
	}
}
