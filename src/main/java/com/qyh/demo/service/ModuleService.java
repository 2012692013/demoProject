package com.qyh.demo.service;


import com.qyh.demo.base.mapper.BaseService;
import com.qyh.demo.entity.Module;
import com.qyh.demo.vo.ModuleVo;
import com.qyh.demo.vo.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ModuleService extends BaseService<Module> {

	/**
	 * 后台查询模块列表
	 * @param request
	 * @param moduleName
	 * @param topModId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * 2018年7月11日
	 * @author yh
	 */
	ResponseResult findListByLimitPageWeb(HttpServletRequest request,
                                          String moduleName, String topModId, Integer pageIndex,
                                          Integer pageSize);

	/**
	 * 后台查询 查询树形结构模块列表
	 * @param request
	 * @return
	 * 2018年7月11日
	 * @author yh
	 */
	ResponseResult findTopModuleWeb(HttpServletRequest request)throws Exception;

	/**
	 * 根据ID查询单个实体（后台）
	 * @param request
	 * @param id
	 * @return
	 * 2018年7月11日
	 * @author yh
	 */
	ResponseResult findOneByLimitWeb(HttpServletRequest request, String id);

	/**
	 * 后台修改实体
	 * @param request
	 * @param entity
	 * @return
	 * 2018年7月11日
	 * @author yh
	 */
	ResponseResult changeEntityWeb(HttpServletRequest request, Module entity) throws Exception ;

	/**
	 * 查询模块  树形结构
	 * @param whereMap
	 * @return
	 * @throws Exception
	 * 2018年7月11日
	 * @author yh
	 */
	List<ModuleVo> findModuleListByLimit(Map<String, Object> whereMap);

	/**
	 * 后台修改模块
	 * @param entity
	 * @return
	 * @throws Exception
	 * 2018年7月11日
	 * @author yh
	 */
	public int updateModule(Module entity) throws Exception;

	/**
	 * 条件统计  用户模块
	 * @param whereMap
	 * @return
	 * 2018年7月11日
	 * @author yh
	 */
	String countModuleNames(Map<String, Object> whereMap);

	/**
	 * 查询模块id集
	 * @param whereMap2
	 * @return
	 * 2018年7月11日
	 * @author yh
	 */
	String findModuleIds(Map<String, Object> whereMap2);


}
