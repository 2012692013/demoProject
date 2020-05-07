package com.qyh.demo.base.controllers;

import com.qyh.demo.base.cache.SysCodeUtil;
import com.qyh.demo.base.util.DataTables;
import com.qyh.demo.base.util.FileUtil;
import com.qyh.demo.base.util.Kit;
import com.qyh.demo.entity.SysCode;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.service.SysCodeService;
import com.qyh.demo.vo.ResponseResult;
import com.qyh.demo.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.qyh.demo.vo.ResponseResult.error;
import static com.qyh.demo.vo.ResponseResult.success;

/**
 * 系统参数
 *
 * @author
 *
 */
@RestController
@RequestMapping("code")
public class CodeController {

    @Autowired
    private SysCodeService codeService;// 系统参数

    /**
     * 获取系统参数列表
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "findListByLimit", method = RequestMethod.GET)
    public ResponseResult findListByLimit(HttpServletRequest request, String type, String codeKey, Integer pageIndex, Integer pageSize) {

        DataTables dt = DataTables.getInstance(request, null);

        List<String> whereList = new ArrayList<>();
        if (StringUtil.isNotEmpty(type)) {
            whereList.add("type like '%"+type+"%'");
        }
        if (StringUtil.isNotEmpty(codeKey)) {
            whereList.add("code_key like '%"+codeKey+"%'");
        }
        whereList.add("del_flag = 0");
        PageHelper.startPage(dt.getPageIndex(),dt.getPageSize());
        List<SysCode> list = codeService.findListByLimit(SysCode.class, whereList, "create_time desc");

        PageInfo<SysCode> pageInfo = new PageInfo<>(list);
        dt.setData(pageInfo.getList());
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("pageIndex", pageInfo.getPageNum());
        pageMap.put("pageSize", dt.getPageSize());
        pageMap.put("total", pageInfo.getTotal());
        pageMap.put("pages", pageInfo.getPages());
        dt.setOtherData(pageMap);

        return ResponseResult.success(dt);
    }

    /**
     * 添加系统参数
     *
     * @param entity
     * @param request
     * @return
     * @author shugl
     * @date 2018年6月4日
     */
    @RequestMapping(value = "sys/saveEntity", method = RequestMethod.POST)
    public ResponseResult saveEntity(SysCode entity, HttpServletRequest request) {
        try {
            if (StringUtil.isEmpty(entity.getId())) {
                List<String> whereList = new ArrayList<>();
                whereList.add("code_key = '"+entity.getCodeKey()+"'");
                whereList.add("type = '"+entity.getType()+"'");
                List<SysCode> list = codeService.findListByLimit(SysCode.class, whereList, null);
                if (list.size() > 0) {
                    return ResponseResult.error("键值不能重复");
                }
                if (entity.getState()==null){
                    entity.setState(1);
                }
                entity.preInsert(request);

                codeService.saveEntity(entity);
                //更新缓存
                SysCodeUtil.updateCode("save", entity, null, null);
                return ResponseResult.success("添加成功");
            }else {
                SysCode oldCode = codeService.findEntityById(entity.getId());
                if (oldCode == null) {
                    return ResponseResult.error("参数不存在");
                }
                List<String> whereList = new ArrayList<>();
                whereList.add("code_key = '"+entity.getCodeKey()+"'");
                whereList.add("type = '"+entity.getType()+"'");
                whereList.add("id !='"+entity.getId()+"'");
                List<SysCode> list = codeService.findListByLimit(SysCode.class, whereList, null);
                if (list.size() > 0) {
                    return ResponseResult.error("键值不能重复");
                }
                entity.preUpdate(request);
                codeService.updateEntityById(entity);
                //更新缓存
                SysCodeUtil.updateCode("update", entity, oldCode.getCodeKey(), oldCode.getType());
                return ResponseResult.success("修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("系统异常");
        }
    }

    /**
     * 根据ID查询信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "findEntityById", method = RequestMethod.GET)
    public ResponseResult findEntityById(HttpServletRequest request, String id) {
        if (StringUtils.isEmpty(id)) {
            return ResponseResult.error("参数未传");
        }
        try {
            SysCode entity = this.codeService.findEntityById(id);
            return ResponseResult.success(entity.getDescription(),entity.getCodeValue());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("系统参数不存在");
        }
    }

    /**
     * 查询所有类型
     *
     * @return
     */
    @RequestMapping(value = "fintTypes", method = RequestMethod.GET)
    public ResponseResult findTypes() {
        Result result = new Result();
        return ResponseResult.success(this.codeService.findListByLimit(SysCode.class, null, null));
    }

    /**
     * 改变实体信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "sys/changeEntity", method = RequestMethod.POST)
    public ResponseResult changeEntity(HttpServletRequest request, SysCode entity) {
        if (entity == null) {
            return ResponseResult.error("参数未传");
        }

        String msg = "";

        Map<String, String> fileMap = FileUtil.upLoadFile("uploadFile/codeFile", request);
        if (fileMap != null && fileMap.size() >= 1) {
            String valueFile = fileMap.get("valueFile");
            entity.setCodeValue(valueFile);
        }

        try {
            if (StringUtils.isEmpty(entity.getId())) {// 添加
                entity.preInsert(request);
                this.codeService.saveEntity(entity);
                msg = "添加成功";
            } else {// 修改
                if (fileMap != null && fileMap.size() >= 1) {
                    SysCode oldEntity = this.codeService.findEntityById(entity.getId());
                    // 删除原文件
                    FileUtil.delFileByPath(request, oldEntity.getCodeValue());
                }
                entity.preUpdate(request);
                this.codeService.updateEntityById(entity);
                msg = "修改成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 操作失败，把上传的文件删除
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                for (String path : entry.getValue().split(",")) {
                    FileUtil.delFileByPath(request, path);
                }
            }
            return ResponseResult.error("系统异常");
        }
        return ResponseResult.success("操作成功");
    }

    /**
     * 删除单个实体
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "sys/deleteEntity", method = RequestMethod.POST)
    public ResponseResult deleteEntity(HttpServletRequest request, String ids) {
        if (StringUtils.isEmpty(ids)) {
            return ResponseResult.error("参数未传");
        }
        SysUser user = Kit.getCurLoginUser(request);

        try {
            SysCode code = codeService.findEntityById(ids);
            if (!"dev".equals(user.getUserType()) && "sys".equals(code.getCodeLevel())) {
                return ResponseResult.error("系统级别无法删除");
            }

            SysCode entity = codeService.findEntityById(ids);
            codeService.deleteById(ids);
            //更新缓存
            SysCodeUtil.updateCode("delete", entity, null, null);

            return ResponseResult.success("删除成功");
        } catch (Exception e) {
            return ResponseResult.error("系统异常");
        }
    }

    /**
     * 修改状态
     *
     * @param request
     */
    @RequestMapping(value = "sys/updateStateById", method = RequestMethod.POST)
    public ResponseResult updateStateById(HttpServletRequest request, SysCode code) {
        if (StringUtils.isNotEmpty(code.getId())) {
            try {
                this.codeService.updateEntityById(code);
                return ResponseResult.success("修改成功");
            } catch (Exception e) {
                return ResponseResult.error("系统异常");
            }
        }
        return ResponseResult.error("参数未传");
    }

    @RequestMapping(value = "findCodeByKey", method = RequestMethod.GET)
    public ResponseResult findByCodeKey(String key, Integer num) {
        if (num == null) {
            num = 1;
        }

        if (StringUtil.isEmpty(key)) {
            return ResponseResult.error("参数未传");
        } else {
            List<String> whereList = new ArrayList<>();
            whereList.add("code_key like '%" + key + "%'");
            PageHelper.startPage(1, num);
            List<SysCode> list = codeService.findListByLimit(SysCode.class, whereList, "create_time desc");

            return ResponseResult.success("获取成功");
        }
    }
}
