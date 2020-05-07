package com.qyh.demo.service.impl;

import com.qyh.demo.base.exception.SMSException;
import com.qyh.demo.base.mapper.BaseServiceImpl;
import com.qyh.demo.base.third.sms.HuaXinSms;
import com.qyh.demo.base.util.JedisUtil;
import com.qyh.demo.base.util.Kit;
import com.qyh.demo.dto.SysUserDto;
import com.qyh.demo.entity.Module;
import com.qyh.demo.entity.Role;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.enums.JedisIndex;
import com.qyh.demo.mapper.SysUserMapper;
import com.qyh.demo.service.ModuleService;
import com.qyh.demo.service.RoleService;
import com.qyh.demo.service.UserService;
import com.qyh.demo.vo.ModuleVo;
import com.qyh.demo.vo.ResponseResult;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.qyh.demo.vo.ResponseResult.error;
import static com.qyh.demo.vo.ResponseResult.success;

@Service
public class UserServiceImpl extends BaseServiceImpl<SysUser> implements UserService {

	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private RoleService roleSer;

	@Autowired
	private ModuleService moduleSer;


	@Override
	public ResponseResult loginPC(HttpServletRequest request, SysUserDto dto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(dto.getAccount()) || StringUtils.isEmpty(dto.getPassword())) {
			return ResponseResult.error();
		}
		// 查询当前账号是否存在
		List<String> whereList = new ArrayList<String>();
		whereList.add("account = '" + dto.getAccount() + "'");
		whereList.add("del_flag = '0'");//未删除
		SysUser user = this.findOneByLimit(SysUser.class, whereList);
		if (user == null) {
			return ResponseResult.error("账号不存在");
		}
		String md5Str = Kit.getMD5Str(dto.getAccount() + dto.getPassword());
		// 判断账号密码
		if (!md5Str.equals(user.getPassword())) {
		    return ResponseResult.error("账号或秘密错误");
		}
		/**
		 * 判断登陆账号是否有登陆权限
		 */
		if (StringUtil.isEmpty(user.getUserType()) || user.getUserType().contains("member")) {
			return ResponseResult.error("账号无权登录");
		}

		// 判断是否被冻结
		if ("0".equals(user.getState())) {// 用户被冻结无法登陆
		    return ResponseResult.error("账号被冻结");
		}


		String oldToken = JedisUtil.getJedisUtil().get(user.getId(), JedisIndex.USER);
		if(StringUtils.isNotEmpty(oldToken)){
			JedisUtil.getJedisUtil().del(oldToken, JedisIndex.USER);
			JedisUtil.getJedisUtil().del(user.getId(),JedisIndex.USER);
		}

		String token = Kit.getMD5Str(System.currentTimeMillis() + user.getAccount());
		JedisUtil.getJedisUtil().setObj(token,user,JedisIndex.USER);
		JedisUtil.getJedisUtil().set(user.getId(),token,JedisIndex.USER);

		user.setToken(token);
		resultMap.put("user", user);
		return ResponseResult.success(resultMap);
	}


	@Override
	public ResponseResult getLoginPrivilege(HttpServletRequest request) {
		List<String> whereList = new ArrayList<String>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		SysUser user = Kit.getCurLoginUser(request);
		whereList.add("state = '1'");
		String moduleIds = "";
		List<Role> roleList = null;
		if (user == null) {
		    return ResponseResult.error("请先登录");
		}
		// 登录用户权限
		if(!("administrator".equals(user.getUserType())||"dev".equals(user.getUserType()))){
			if(StringUtils.isEmpty(user.getRoleIds())) {
			    return ResponseResult.error("用户还未设置权限，请先创建好模块，添加管理员角色，或系统固定角色模块，再创建相应用户");
			}
			whereList.add("id in " + Kit.getWhereByIds(user.getRoleIds()));
			roleList = this.roleSer.findListByLimit(Role.class, whereList, null);

			Set<String> set = new HashSet<String>();
			for (Role r : roleList) {
				for (String mId : r.getModuleIds().split(",")) {
					set.add(mId);
				}
			}
			moduleIds = set.toString().substring(1, set.toString().length() - 1).replace(" ", "");
		}else {
			List<Module> modules = moduleSer.findListByLimit(Module.class, null, null);
			for(Module m : modules){
				moduleIds += m.getId()+",";
			}
			if (moduleIds.length() > 0) {
				moduleIds = moduleIds.substring(0, moduleIds.length() - 1);
			}
		}


		whereList.clear();

		whereList.add("pid = '0'");
		if (tk.mybatis.mapper.util.StringUtil.isNotEmpty(moduleIds)) {
			whereList.add("id in "+Kit.getWhereByIds(moduleIds));
		}


		Map<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("whereList", whereList);
		whereMap.put("orderBy", "number asc");
		whereMap.put("moduleIds", moduleIds);

		List<ModuleVo> moduleList = this.moduleSer.findModuleListByLimit(whereMap);
		resultMap.put("moduleList", moduleList);
		return ResponseResult.success("登录成功",moduleList);
	}

	@Override
	public ResponseResult exitLoginPC(HttpServletRequest request) {
		String token = request.getHeader("token");
		if (StringUtils.isNotEmpty(token)) {// 删除web端 登录缓存
			Object obj = JedisUtil.getJedisUtil().getObject(token, JedisIndex.USER);
			JedisUtil.getJedisUtil().del(token, JedisIndex.USER);
			JedisUtil.getJedisUtil().del(((SysUser)obj).getId(),JedisIndex.USER);
		}

		return ResponseResult.success();
	}




	/**********************app*************************/
	@Override
	public ResponseResult getMsgCode(SysUserDto userDto)  {

		if(userDto==null||StringUtils.isEmpty(userDto.getAccount())||
				StringUtils.isEmpty(userDto.getSecret())){
		    return ResponseResult.error();
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		System.out.println(new Date().getTime());
		//判断加密是否正确  加密方法:用户账号+接口返回时间戳+"bg"(加密盐) 进行md5

		String timeCache = JedisUtil.getJedisUtil().get(userDto.getAccount()+"timestamp",JedisIndex.MSG_CODE);
		if(StringUtils.isEmpty(timeCache)){
		    return ResponseResult.error("获取验证码超时，请重试");
		}
		System.out.println("账号="+userDto.getAccount()+"时间戳="+timeCache+"盐=demo");
		if(!userDto.getSecret().equals(Kit.getMD5Str(userDto.getAccount()+timeCache+"demo"))){
		    return ResponseResult.error("验证码错误");
		}
		//清除本次校验秘钥
		JedisUtil.getJedisUtil().del(userDto.getAccount()+"timestamp",JedisIndex.MSG_CODE);

		String code = Kit.getMsgCode();
		resultMap.put("code", code);
		//发送短信
		try {
			HuaXinSms.sendSMS(userDto.getAccount(),code,"code");
		} catch (Exception e1) {
			throw new SMSException("短信发送异常", e1);
		}
		System.out.println("======================验证码："+code+"==================");
		//将验证码存入缓存
		JedisUtil.getJedisUtil().set(userDto.getAccount()+"msgCode",code,JedisIndex.MSG_CODE);
		JedisUtil.getJedisUtil().expire(userDto.getAccount()+"msgCode",300,JedisIndex.MSG_CODE);
		return ResponseResult.success("获取验证码成功",code);
	}

}
