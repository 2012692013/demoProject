package com.qyh.demo.base.config.init;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 初始化全局bean
 * @author qiuyuehao
 * 2018年6月12日
 */

@Configuration
@ConfigurationProperties(prefix="init")
@PropertySource("classpath:init.properties")
@Data
public class InitBean {

	private String name;
	private String type;

	private String neteaseAppKey = "6b20b0e1aa20bc9df38182006d15cXXX";//网易企业APPkey
	private String neteaseAppSecret = "1f034824eXXX";//网易企业AppSecret
	public String getNeteaseAppKey() {
		return neteaseAppKey;
	}
	public void setNeteaseAppKey(String neteaseAppKey) {
		this.neteaseAppKey = neteaseAppKey;
	}
	public String getNeteaseAppSecret() {
		return neteaseAppSecret;
	}
	public void setNeteaseAppSecret(String neteaseAppSecret) {
		this.neteaseAppSecret = neteaseAppSecret;
	}
}
