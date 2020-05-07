package com.qyh.demo.base.third.pay.wechat.message;

import java.util.Map;

/**
 * 微信消息
 * @author Administrator
 *
 */
public class WxMessage {
	
	//接收者openid
	private String touser;
	//模板ID
	private String template_id;
	//模板数据
	private Map<String,Property> data;
	
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public Map<String, Property> getData() {
		return data;
	}
	public void setData(Map<String, Property> data) {
		this.data = data;
	}
}
