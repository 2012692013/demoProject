package com.qyh.demo.base.controllers;

import com.qyh.demo.base.util.Kit;
import com.qyh.demo.entity.Module;
import com.qyh.demo.entity.Role;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.service.ModuleService;
import com.qyh.demo.service.RoleService;
import com.qyh.demo.service.UserService;
import com.qyh.demo.vo.ModuleVo;
import com.qyh.demo.vo.ResponseResult;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.qyh.demo.vo.ResponseResult.error;
import static com.qyh.demo.vo.ResponseResult.success;

/**
 * 模块
 * @sysor Administrator
 *
 */
@RestController
@RequestMapping("module")
public class ModuleController {


	@Resource
	private ModuleService modSer;
	@Autowired
	private UserService userSer;
	@Autowired
	private RoleService roleSer;

	/**
	 * 根据条件查询所有实体
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/findListByLimitPageWeb",method=RequestMethod.POST)
	public ResponseResult findListByLimitPageWeb(HttpServletRequest request, String moduleName, String topModId,
                                                 @RequestParam(required=false,defaultValue="1")Integer pageIndex,
                                                 @RequestParam(required=false,defaultValue="10")Integer pageSize) throws Exception{
		try {
			SysUser curLoginUser = Kit.getCurLoginUser(request);
			if(curLoginUser == null){
				return ResponseResult.error("请先登录");
			}

			//List<Module> list = this.modSer.findListByLimit(Module.class, whereList, "grade ASC,number ASC");
			return this.modSer.findListByLimitPageWeb(request,moduleName,topModId,pageIndex,pageSize);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}
	}



	/**
	 * 查询树形结构模块列表
	 * @param request
	 * @return
	 * @throws Exception
	 * 2018年5月22日
	 * @sysor yh
	 */
	@RequestMapping(value="/sys/findTopModuleWeb",method=RequestMethod.POST)
	public ResponseResult findTopModuleWeb(HttpServletRequest request) throws Exception{
		try {
			SysUser curLoginUser = Kit.getCurLoginUser(request);
			if(curLoginUser == null){
			    return ResponseResult.error("请先登录");
			}

			return this.modSer.findTopModuleWeb(request);

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
			return this.modSer.findOneByLimitWeb(request,id);
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
	public ResponseResult changeEntityWeb(HttpServletRequest request, Module entity) throws Exception{
		try {
			return this.modSer.changeEntityWeb(request,entity);

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
	public ResponseResult deleteEntityWeb(HttpServletRequest request,String id){
		if(StringUtils.isNotEmpty(id)){
			try {
				List<String> whereList = new ArrayList<String>();
				whereList.add("pid = '"+id+"'");
				List<Module> list = this.modSer.findListByLimit(Module.class, whereList, null);
				String ids = id;
				for(Module m : list){
					ids += ","+m.getId();
				}
				whereList.clear();
				whereList.add("id in "+Kit.getWhereByIds(ids));
				this.modSer.deleteByLimit(Module.class, whereList);
				return ResponseResult.success("操作成功");
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseResult.error("系统异常");
			}
		}else {
		    return ResponseResult.error();
        }
	}

	/**
	 * 修改状态
	 * @param request
	 */
	@RequestMapping(value="/sys/updateStateById",method=RequestMethod.POST)
	public ResponseResult updateStateById(HttpServletRequest request,Module entity) throws Exception{
		if(StringUtils.isNotEmpty(entity.getId())){
			try {
				this.modSer.updateEntityById(entity);
				return ResponseResult.success("操作成功");
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseResult.error("系统异常");
			}
		}else{
		    return ResponseResult.error();
        }
	}

	/**
	 * 获取登录人菜单
	 * @param request
	 * @return
	 * @author shugl
	 * @date 2018年5月25日
	 */
	@RequestMapping(value="/sys/getLoginModules",method=RequestMethod.GET)
	public ResponseResult getLoginModules(HttpServletRequest request){

		SysUser login = userSer.findEntityById(Kit.getCurLoginUser(request).getId());
		String moduleIds = "";
		List<String> whereList = new ArrayList<>();
		whereList.add("state = '1'");
		List<Role> roleList = null;
		if (!"dev".equals(login.getUserType())) {
			if (!StringUtil.isEmpty(login.getRoleIds())) {
				whereList.add("id in "+Kit.getWhereByIds(login.getRoleIds()));
			}
			roleList = roleSer.findListByLimit(Role.class, whereList, null);

			Set<String> set = new HashSet<String>();

			for(Role r : roleList){
				for(String mId : r.getModuleIds().split(",")){
					set.add(mId);
				}
			}
			moduleIds = set.toString().substring(1,set.toString().length()-1).replace(" ", "");
		}else {//开发者账号菜单
			List<Module> modules = modSer.findListByLimit(Module.class, null, null);
			for(Module m : modules){
				moduleIds += m.getId()+",";
			}
			if (moduleIds.length() > 0) {
				moduleIds = moduleIds.substring(0, moduleIds.length() - 1);
			}
		}


		whereList.clear();
		if (StringUtil.isNotEmpty(moduleIds)) {
			whereList.add("id in "+Kit.getWhereByIds(moduleIds));
		}
		whereList.add("pid = '0'");

		Map<String,Object> whereMap = new HashMap<String, Object>();
		whereMap.put("whereList", whereList);
		whereMap.put("orderBy", "number asc");
		whereMap.put("moduleIds", moduleIds);

		try {
			List<ModuleVo> moduleList = modSer.findModuleListByLimit(whereMap);
			return ResponseResult.success("获取成功",moduleList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseResult.error("系统异常");
		}
	}

}
