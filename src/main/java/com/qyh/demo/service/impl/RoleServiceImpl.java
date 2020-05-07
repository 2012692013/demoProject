package com.qyh.demo.service.impl;

import com.qyh.demo.base.mapper.BaseServiceImpl;
import com.qyh.demo.base.util.Kit;
import com.qyh.demo.base.util.MapUtil;
import com.qyh.demo.entity.Module;
import com.qyh.demo.entity.Role;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.mapper.RoleMapper;
import com.qyh.demo.service.ModuleService;
import com.qyh.demo.service.RoleService;
import com.qyh.demo.service.UserService;
import com.qyh.demo.vo.ModuleVo;
import com.qyh.demo.vo.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper dao;

    @Autowired
    private UserService userSer;//用户

    @Resource
    private ModuleService modSer;

    @Override
    public ResponseResult findListByLimitPageWeb(HttpServletRequest request,
                                                 String roleName, String moduleIds, Integer pageIndex,
                                                 Integer pageSize) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Map<String,Object> whereMap = new HashMap<String,Object>();
        List<String> whereList = new ArrayList<String>();
        if(StringUtils.isNotEmpty(roleName)){
            whereList.add("role_name like '%"+roleName+"%'");
        }
        if(StringUtils.isNotEmpty(moduleIds)){//
            List<String> whereList2 = new ArrayList<String>();
            whereList2.add("module_name like '%"+moduleIds+"%'");
            List<Module> modules = this.modSer.findListByLimit(Module.class, whereList2, null);
            String moduleWhere = "";
            if(modules != null && !modules.isEmpty()) {

                for(Module m : modules) {
                    moduleWhere = moduleWhere + "module_ids like '%"+m.getId()+"%'" + " or ";
                }
                if(StringUtils.isNotEmpty(moduleWhere)) {
                    moduleWhere = moduleWhere.substring(0, moduleWhere.length() - 3);
                    whereList.add("("+moduleWhere+")");
                }
            }else{
                moduleWhere = "module_ids = ''";
            }

        }
        whereMap.put("whereList", whereList);
        whereMap.put("orderBy", "create_time desc");
        PageHelper.startPage(pageIndex, pageSize,true);
        List<Map<String,Object>> listMap = this.dao.findListByLimitPageWeb(whereMap);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>(listMap);
        if(listMap != null && !listMap.isEmpty()) {
            listMap = MapUtil.getHumpKey(listMap);
            for(Map<String,Object> m : listMap) {
                String moduleNames = "";
                Object object = m.get("moduleIds");
                if(object != null){
                    whereMap.clear();
                    whereList.clear();
                    if(StringUtils.isNotEmpty(object.toString())) {
                        whereList.add("id in "+ Kit.getWhereByIds(object.toString()));
                        whereMap.put("whereList", whereList);
                        moduleNames = this.modSer.countModuleNames(whereMap);
                    }
                }
                m.put("moduleNames", moduleNames);

            }
        }
        resultMap.put("listMap", listMap);
        resultMap.put("totalPage", pageInfo.getPages());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("pageIndex", pageIndex);
        return ResponseResult.success("操作成功",resultMap);
    }

    @Override
    public ResponseResult findAllRoleWeb() {
        Map<String,Object> whereMap = new HashMap<String,Object>();
        List<String> whereList = new ArrayList<String>();
        whereMap.put("orderBy","create_time desc");
        whereMap.put("whereList", whereList);
        //已驼峰转化
        List<Map<String,Object>> listMap = this.dao.findAllRoleWeb(whereMap);
        return ResponseResult.success("操作成功",listMap);
    }

    @Override
    public ResponseResult findEntityByIdWeb(String id) throws Exception {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(id)){
            Role entity = this.findEntityById(id);
            resultMap.put("entity", entity);
        }
        List<String> whereList = new ArrayList<String>();
        whereList.clear();
        whereList.add("pid = '0'");
        Map<String,Object> whereMap = new HashMap<String, Object>();
        whereMap.put("whereList", whereList);
        whereMap.put("orderBy", "number asc");
        Map<String,Object> whereMap2 = new HashMap<String,Object>();
        String moduleIds = this.modSer.findModuleIds(whereMap2);
        whereMap.put("moduleIds", moduleIds);
        List<ModuleVo> moduleList = this.modSer.findModuleListByLimit(whereMap);
        resultMap.put("modules", moduleList);
        return ResponseResult.success("操作成功",resultMap);
    }

    @Override
    public ResponseResult changeEntityWeb(HttpServletRequest request, Role entity) {
        SysUser curLoginUser = Kit.getCurLoginUser(request);
        if(curLoginUser == null){
            return ResponseResult.error("请先登录");
        }
        List<String> whereList = new ArrayList<String>();
        if(StringUtils.isNotEmpty(entity.getId())){
            Role role = this.findEntityById(entity.getId());
            if(!entity.getRoleName().equals(role.getRoleName())) {
                //有修改角色名
                whereList.add("role_name like '%"+role.getRoleName()+"%'");
                List<SysUser> users = this.userSer.findListByLimit(SysUser.class, whereList, null);
                if(users != null && !users.isEmpty()){
                    SysUser newUser = new SysUser();
                    for(SysUser su : users) {
                        newUser.setId(null);
                        newUser.setRoleName(null);
                        newUser.setId(su.getId());
                        newUser.setRoleName(su.getRoleName().replace(role.getRoleName(), entity.getRoleName()));
                        newUser.preUpdate(request);
                        this.userSer.updateEntityById(newUser);
                    }
                }
            }
            entity.preUpdate(request);
            this.updateEntityById(entity);

        }else{
            entity.setCreateTime(new Date());
            entity.setState(1);
            entity.setId(null);
            entity.preInsert(request);
            this.saveEntity(entity);
        }
        return ResponseResult.success("操作成功");
    }
	
}
