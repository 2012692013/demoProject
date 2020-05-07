package com.qyh.demo.base.third.pay.wechat.message;

/**
 * 微信模板的属性
 * @author Administrator
 *
 */
public class Property {
	//值
	private String value;
	//颜色
	private String color;
	
	public Property(String value) {
		this.value = value;
		this.color = "#173177";
	}
	
	public Property(String value,String color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}
