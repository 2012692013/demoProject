package com.qyh.demo.entity;

import com.qyh.demo.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "sys_user")
@Data
public class SysUser extends BaseEntity implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 账号
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 令牌
     */
    private String token;

    /**
     * 角色ID集
     */
    @Column(name = "role_ids")
    private String roleIds;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * member:普通用户   administrator:超级管理员  admin:子管理员  dev:开发者
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    @Column(name = "head_img")
    private String headImg;

    private Integer state;


}