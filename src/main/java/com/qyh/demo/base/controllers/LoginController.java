package com.qyh.demo.base.controllers;

import com.qyh.demo.base.util.bucket.TouristApi;
import com.qyh.demo.dto.SysUserDto;
import com.qyh.demo.service.UserService;
import com.qyh.demo.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录/退出
 * @author qiuyuehao
 *
 */
@RestController
@RequestMapping("userLogin")
public class LoginController {
	
	@Autowired
	private UserService userSer;

	/**
	 * PC登录
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@TouristApi
	@RequestMapping(value="/loginWeb",method=RequestMethod.POST,headers="api-version=1")
	public ResponseResult loginPC(HttpServletRequest request, SysUserDto userDto){
		return userSer.loginPC(request,userDto);
	}
	
	/**
	 * 获取登录用户权限信息
	 * @param request
	 * @return
	 * 2018年5月23日
	 * @author qiuyuehao
	 */
	@RequestMapping(value="/sys/getLoginPrivilege",method=RequestMethod.GET,headers="api-version=1")
	public ResponseResult getLoginPrivilege(HttpServletRequest request){
		return userSer.getLoginPrivilege(request);
	}

	
	/**
	 * PC退出登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/exitLoginPC",method=RequestMethod.POST,headers="api-version=1")
	public ResponseResult exitLoginPC(HttpServletRequest request){
		return userSer.exitLoginPC(request);
	}

	
}
