package com.qyh.demo.base.controllers;

import com.qyh.demo.base.util.Kit;
import com.qyh.demo.entity.Role;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.service.ModuleService;
import com.qyh.demo.service.RoleService;
import com.qyh.demo.service.UserService;
import com.qyh.demo.vo.ResponseResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 * @sysor Administrator
 *
 */
@RestController
@RequestMapping("role")
public class RoleController {
	@Resource
	private RoleService rolSer;
	@Resource
	private ModuleService modSer;
	@Resource
	private UserService useSer;

	/**
	 * 根据条件查询所有实体
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/findListByLimitPageWeb",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult findListByLimitPageWeb(HttpServletRequest request, String roleName, String moduleIds,
                                                 @RequestParam(required=false,defaultValue="1")Integer pageIndex,
                                                 @RequestParam(required=false,defaultValue="10")Integer pageSize) throws Exception{
		try {
			SysUser curLoginUser = Kit.getCurLoginUser(request);
			if(curLoginUser == null){
				return ResponseResult.error("请先登录");
			}
			return this.rolSer.findListByLimitPageWeb(request,roleName,moduleIds,pageIndex,pageSize);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}

	}

	/**
	 * 查询所有角色
	 * @param request
	 * @return
	 * @throws Exception
	 * 2018年5月28日
	 * @sysor yh
	 */
	@RequestMapping(value="/sys/findAllRoleWeb",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult findAllRoleWeb(HttpServletRequest request ) throws Exception{
		try {
			SysUser curLoginUser = Kit.getCurLoginUser(request);
			if(curLoginUser == null){
				return ResponseResult.error("请先登录");
			}
			return this.rolSer.findAllRoleWeb();

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}

	}


	/**
	 * 根据ID查询单个实体
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/findOneByLimitWeb",method=RequestMethod.GET)
	public ResponseResult findOneByLimitWeb(HttpServletRequest request,String id) throws Exception{
		try {
			return this.rolSer.findEntityByIdWeb(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}
	}

	/**
	 * 改变实体
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/changeEntityWeb",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult changeEntityWeb(HttpServletRequest request, Role entity) throws Exception{
		try {
			return this.rolSer.changeEntityWeb(request,entity);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}
	}


	/**
	 * 删除实体
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/deleteEntityWeb",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult deleteEntityWeb(HttpServletRequest request,String roleId){
		try {
			if(StringUtils.isNotEmpty(roleId)){
				List<String> whereList = new ArrayList<String>();
				whereList.add("role_ids like '%"+roleId+"%'");
				List<SysUser> list = this.useSer.findListByLimit(SysUser.class, whereList, null);
				if(list != null && list.size() >= 1){
				    return ResponseResult.error("该角色正在被使用");
				}
				this.rolSer.deleteById(roleId);
				return ResponseResult.success("操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}
		return ResponseResult.error();
	}

	/**
	 * 修改状态
	 * @param request
	 */
	@RequestMapping(value="/sys/updateStateById",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult updateStateById(HttpServletRequest request,Role entity) throws Exception{

		try {
			if(StringUtils.isNotEmpty(entity.getId())){
				this.rolSer.updateEntityById(entity);
				return ResponseResult.success();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}
		return ResponseResult.error();
	}

	/**
	 * 验证角色是否正被使用
	 * @param request
	 */
	@RequestMapping(value="/sys/verifyRole",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult verifyRole(HttpServletRequest request,Role entity) throws Exception{
		try {
			String isUse = "0";
			if(StringUtils.isNotEmpty(entity.getId())){
				List<String> whereList = new ArrayList<String>();
				whereList.add("role_ids like '%"+entity.getId()+"%'");

				List<SysUser> list = this.useSer.findListByLimit(SysUser.class, whereList, null);
				if(list != null && list.size() >= 1){
					isUse = "1";
				}
			}
			return ResponseResult.success("操作成功",isUse);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}
	}
}
