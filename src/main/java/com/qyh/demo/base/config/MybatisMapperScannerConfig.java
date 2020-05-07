package com.qyh.demo.base.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
@AutoConfigureAfter(DBConfig.class)
public class MybatisMapperScannerConfig {
	
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		mapperScannerConfigurer.setBasePackage("com.qyh.demo.mapper");
		//配置通用Mapper，详情请查阅官方文档
		Properties properties = new Properties();
		properties.setProperty("mappers", Mapper.class.getName());
		properties.setProperty("notEmpty", "false");//insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
		properties.setProperty("IDENTITY", "sys_guid()");//使用UUID作為主鍵
		properties.setProperty("ORDER","BEFORE");//将查询主键作为前置操作
		mapperScannerConfigurer.setProperties(properties);
		return mapperScannerConfigurer;
	}
}
