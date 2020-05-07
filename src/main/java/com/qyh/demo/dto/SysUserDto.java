package com.qyh.demo.dto;

import com.qyh.demo.entity.SysUser;
import lombok.Data;

import javax.persistence.Transient;

@Data
public class SysUserDto extends SysUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	private String secret;//验证码加密(服务器返回时间戳+项目名称进行MD5)
	
	@Transient
	private String type;//验证码类型  register注册  pwd忘记密码 thirdBind三方绑定
	

	@Transient
	private String msgCode;//短信验证码
	
	@Transient
	private String newPwd;//新密码
	
}
