package com.qyh.demo.base.controllers;

import com.qyh.demo.base.util.DataTables;
import com.qyh.demo.base.util.JedisUtil;
import com.qyh.demo.base.util.Kit;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.enums.JedisIndex;
import com.qyh.demo.service.UserService;
import com.qyh.demo.vo.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
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
 * Created with IDEA
 * author:huxi
 * Date:2019/1/3
 * Time:11:55 AM
 */
@Transactional
@Slf4j
@RestController
@RequestMapping("sys/user")
public class UserController {


    @Autowired
    UserService userService;


    /**
     * 根据条件分页查询用户列表
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "findAdminByLimit", method = RequestMethod.GET)
    public ResponseResult findListByLimitPage(HttpServletRequest request, String account, String nickname, String userType, Integer state) {
        List<String> whereList = new ArrayList<>();

        if (StringUtils.isBlank(userType)) {
            return ResponseResult.error();
        }

        if (StringUtil.isNotEmpty(account)) {
            whereList.add("account like '" + account + "%'");
        }
        if (StringUtil.isNotEmpty(nickname)) {
            whereList.add("nickname like '%" + nickname + "%'");
        }
        if (state != null) {
            whereList.add("state = '" + state + "'");
        }
        whereList.add("user_type = '" + userType + "' and user_type not in ('dev','administrator')");

        DataTables dt = DataTables.getInstance(request, null);
        try {
            PageHelper.startPage(dt.getPageIndex(), dt.getPageSize());
            List<SysUser> list = this.userService.findListByLimit(SysUser.class, whereList, "create_time desc");
            PageInfo<SysUser> pageInfo = new PageInfo<SysUser>(list);

            dt.setData(list);
            Map<String, Object> pageMap = new HashMap<>();
            pageMap.put("pageIndex", pageInfo.getPageNum());
            pageMap.put("pageSize", dt.getPageSize());
            pageMap.put("total", pageInfo.getTotal());
            pageMap.put("pages", pageInfo.getPages());
            dt.setOtherData(pageMap);

            return ResponseResult.success(dt);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            return ResponseResult.error("系统异常");
        }

    }

    /**
     * 修改用户冻结状态
     * @author LLH
     * @param request
     * @param id
     * @param state
     * @return
     */
    @RequestMapping("changeState")
    public ResponseResult updateUserState(HttpServletRequest request, String id, Integer state) {
        SysUser dto = this.userService.findEntityById(id);
        if (dto == null) {
            return ResponseResult.error();
        }
        if("0".equals(state)){//如果是冻结用户,则清除token
            SysUser user = this.userService.findEntityById(id);

            if(StringUtils.isNotBlank(user.getToken())){
                JedisUtil.getJedisUtil().del(user.getToken());
            }

            String sysToken = JedisUtil.getJedisUtil().get(user.getId(), JedisIndex.USER);
            if(StringUtils.isNotEmpty(sysToken)){
                JedisUtil.getJedisUtil().del(sysToken,JedisIndex.USER);
                JedisUtil.getJedisUtil().del(user.getId(),JedisIndex.USER);
            }
        }

        dto.setState(state);
        dto.preUpdate(request);
        if (this.userService.updateEntityById(dto) > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.error();
    }

    /**
     * 添加修改、启用冻结后台用户
     * @param request
     * @param entity
     * @return
     */
    @RequestMapping(value = "changeEntity", method = RequestMethod.POST)
    public ResponseResult changeEntity(HttpServletRequest request, SysUser entity) {
        String msg = "";

        if (StringUtil.isEmpty(entity.getId())) {
            List<String> whereList = new ArrayList<>();
            whereList.add("account = '" + entity.getAccount() + "'");
            SysUser user = userService.findOneByLimit(SysUser.class, whereList);
            if (user != null) {
                return ResponseResult.error("该账号已存在");
            }
            entity.setPassword(Kit.getMD5Str(entity.getAccount()
                    + "123456"));
            entity.preInsert(request);
            this.userService.saveEntity(entity);
            msg = "添加成功";
        } else {
            if (StringUtil.isNotEmpty(entity.getPassword())) {
                entity.setPassword(
                        Kit.getMD5Str(userService.findEntityById(entity.getId()).getAccount() + entity.getPassword()));
            }

            if(entity.getState()!=null&&0==entity.getState()){//如果是冻结用户,则清除token
                SysUser user = this.userService.findEntityById(entity.getId());

                if(StringUtils.isNotBlank(user.getToken())){
                    JedisUtil.getJedisUtil().del(user.getToken());
                }
                String sysToken = JedisUtil.getJedisUtil().get(user.getId(),JedisIndex.USER);
                if(StringUtils.isNotEmpty(sysToken)){
                    JedisUtil.getJedisUtil().del(sysToken,JedisIndex.USER);
                    JedisUtil.getJedisUtil().del(user.getId(),JedisIndex.USER);
                }
            }

            entity.preUpdate(request);
            this.userService.updateEntityById(entity);
            msg = "修改成功";
        }

        return ResponseResult.success(200,msg);

    }


    /**
     * 删除后台用户
     * @param request
     * @param id
     * @return
     */
    @PostMapping("deleteEntity")
    public ResponseResult deleteEntity(HttpServletRequest request, String id){
        if (StringUtil.isEmpty(id)){
            return ResponseResult.error();
        }
        SysUser user = Kit.getCurLoginUser(request);
        if (id.equals(user.getId())){
            return ResponseResult.error("不能删除自己");
        }

        SysUser oldUser = this.userService.findEntityById(id);

        userService.deleteById(id);

        //清除app缓存
        if(oldUser!=null&&StringUtils.isNotEmpty(oldUser.getToken())){
            JedisUtil.getJedisUtil().del(oldUser.getToken(),JedisIndex.USER);
            JedisUtil.getJedisUtil().del(oldUser.getId(),JedisIndex.USER);
        }

        return ResponseResult.success();
    }


}
