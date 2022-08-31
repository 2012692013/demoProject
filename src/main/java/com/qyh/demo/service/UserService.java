package com.qyh.demo.service;

import com.qyh.demo.base.mapper.BaseService;
import com.qyh.demo.dto.SysUserDto;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.vo.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends BaseService<SysUser> {

	/**
	 * pc 登录
	 * @param request
	 * @param userDto
	 * @return
	 */
	public ResponseResult loginPC(HttpServletRequest request, SysUserDto userDto);


	/**
	 * 获取登录用户权限
	 * @param request
	 * @return
	 */
	public ResponseResult getLoginPrivilege(HttpServletRequest request);

	/**
	 * pc  退出登录
	 * @param request
	 * @return
	 */
	public ResponseResult exitLoginPC(HttpServletRequest request);


	/************************app****************************/

	/**
	 * 获取验证码
	 * @param userDto
	 * @return
	 * 2018年9月26日
	 * @author qiuyuehao
	 */
	public ResponseResult getMsgCode(SysUserDto userDto);

}
