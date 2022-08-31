package com.qyh.demo.mapper;

import com.qyh.demo.entity.Role;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends Mapper<Role> {
    /**
     * 查询角色列表
     * @param whereMap
     * @return
     * 2018年10月15日
     * @author qiuyuehao
     */
    List<Map<String, Object>> findListByLimitPageWeb(Map<String, Object> whereMap);

    /**
     * 查询所有角色
     * @param whereMap
     * @return
     * 2018年10月15日
     * @author qiuyuehao
     */
    List<Map<String, Object>> findAllRoleWeb(Map<String, Object> whereMap);
}