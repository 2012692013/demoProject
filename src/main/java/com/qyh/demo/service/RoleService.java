package com.qyh.demo.service;

import com.qyh.demo.base.mapper.BaseService;
import com.qyh.demo.entity.Role;
import com.qyh.demo.vo.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface RoleService extends BaseService<Role> {
    /**
     * 后台查询角色列表
     * @param request
     * @param roleName
     * @param moduleIds
     * @param pageIndex
     * @param pageSize
     * @return
     * 2018年10月15日
     * @author yh
     */
    ResponseResult findListByLimitPageWeb(HttpServletRequest request, String roleName,
                                          String moduleIds, Integer pageIndex, Integer pageSize);


    /**
     * 查询所有角色
     * @return
     * 2018年10月15日
     * @author yh
     */
    ResponseResult findAllRoleWeb();


    /**
     * 查询   角色详情
     * @param id
     * @return
     * 2018年10月15日
     * @author yh
     */
    ResponseResult findEntityByIdWeb(String id) throws Exception;


    /**
     * 添加修改角色
     * @param request
     * @param entity
     * @return
     * 2018年10月15日
     * @author yh
     */
    ResponseResult changeEntityWeb(HttpServletRequest request, Role entity);
	
}
