package com.qyh.demo.service.impl;

import com.qyh.demo.base.mapper.BaseServiceImpl;
import com.qyh.demo.base.util.Kit;
import com.qyh.demo.base.util.MapUtil;
import com.qyh.demo.entity.Module;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.mapper.ModuleMapper;
import com.qyh.demo.service.ModuleService;
import com.qyh.demo.vo.ModuleVo;
import com.qyh.demo.vo.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.qyh.demo.vo.ResponseResult.error;
import static com.qyh.demo.vo.ResponseResult.success;

@Service
public class ModuleServiceImpl extends BaseServiceImpl<Module> implements ModuleService {

	@Autowired
	private ModuleMapper dao;

	@Override
	public ResponseResult findListByLimitPageWeb(HttpServletRequest request,
                                                 String moduleName, String topModId, Integer pageIndex,
                                                 Integer pageSize) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<String> whereList = new ArrayList<String>();

		if(StringUtils.isNotEmpty(moduleName)){
			whereList.add("m.module_name like '%"+moduleName+"%'");
		}
		if(StringUtils.isNotEmpty(topModId)){
			whereList.add("(m.id = '"+topModId+"' or m.pid = '"+topModId+"')");
		}
		Map<String,Object> whereMap = new HashMap<String,Object>();
		whereMap.put("whereList", whereList);
		PageHelper.startPage(pageIndex, pageSize,true);
		List<Map<String,Object>> listMap = this.dao.findListByLimitPageWeb(whereMap);
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>(listMap);
		if(listMap != null && !listMap.isEmpty()) {
			listMap = MapUtil.getHumpKey(listMap);
		}
		resultMap.put("listMap", listMap);
		resultMap.put("totalPage", pageInfo.getPages());
		resultMap.put("pageIndex", pageIndex);
		resultMap.put("total", pageInfo.getTotal());
		whereList.clear();
		whereList.add("m.pid = '0'");
		whereMap.clear();
		whereMap.put("whereList", whereList);
		whereMap.put("orderBy", "m.number ASC");
		//得到所有顶层模块
		resultMap.put("topModules", this.dao.findListByLimitPageWeb(whereMap));
		return ResponseResult.success("操作成功",resultMap);
	}

	@Override
	public ResponseResult findTopModuleWeb(HttpServletRequest request) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<String> whereList = new ArrayList<String>();
		whereList.clear();
		whereList.add("pid = '0'");

		Map<String,Object> whereMap = new HashMap<String, Object>();
		whereMap.put("whereList", whereList);
		whereMap.put("orderBy", "number asc");
		Map<String,Object> whereMap2 = new HashMap<String,Object>();
		String moduleIds = this.findModuleIds(whereMap2);
		whereMap.put("moduleIds", moduleIds);
		List<ModuleVo> moduleList = this.findModuleListByLimit(whereMap);
		resultMap.put("modules", moduleList);
		return ResponseResult.success("操作成功",resultMap);
	}
	@Override
	public String findModuleIds(Map<String, Object> whereMap2) {
		return this.dao.findModuleIds(whereMap2);
	}

	@Override
	public List<ModuleVo> findModuleListByLimit(Map<String,Object> whereMap){
		return this.dao.findModuleListByLimit(whereMap);
	}

	@Override
	public ResponseResult findOneByLimitWeb(HttpServletRequest request, String id) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(id)){
			//Module entity = this.findEntityById(id);
			Map<String,Object> entity =this.dao.findOneByLimitWeb(id);
			if(entity != null) {
				entity = MapUtil.mapKeyUnderlineToHump(entity);
			}
			resultMap.put("entity", entity);
		}
		Map<String,Object> whereMap = new HashMap<String, Object>();
		List<String> whereList = new ArrayList<String>();
		//得到所有顶层模块
		//this.findListByLimit(Module.class, whereList, "grade ASC,number ASC")
		whereList.clear();
		whereList.add("m.pid = '0'");
		whereMap.clear();
		whereMap.put("whereList", whereList);
		whereMap.put("orderBy", "m.number ASC");
		resultMap.put("topModules", this.dao.findListByLimitPageWeb(whereMap));
		return ResponseResult.success("操作成功",resultMap);
	}

	@Override
	public ResponseResult changeEntityWeb(HttpServletRequest request, Module entity) throws Exception {
		SysUser curLoginUser = Kit.getCurLoginUser(request);
		if(curLoginUser == null){
		    return ResponseResult.error("请先登录");
		}
		if(StringUtils.isNotEmpty(entity.getId())){
			entity.preUpdate(request);
			this.updateModule(entity);
		}else{
			entity.setCreateMan(Kit.getCurLoginUser(request).getId());
			entity.setCreateTime(new Date());
			entity.setState(1);
			entity.preInsert(request);
			if (StringUtil.isEmpty(entity.getPid())){
				entity.setPid("0");
			}
			this.saveEntity(entity);
		}
		return ResponseResult.success("操作成功");
	}

	@Override
	public int updateModule(Module entity) throws Exception {
		/*
		Module oldEntity = this.findEntityById(entity.getId());
		
		if(oldEntity.getNumber() != entity.getNumber() && "0".equals(oldEntity.getPid())){
			this.dao.updateModuleNumber(entity.getId(), entity.getNumber());
		}*/

		return this.updateEntityById(entity);
	}

	@Override
	public String countModuleNames(Map<String, Object> whereMap) {
		return this.dao.countModuleNames(whereMap);
	}
	
}
