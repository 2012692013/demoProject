package com.qyh.demo.entity;

import com.qyh.demo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Role extends BaseEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 角色名
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 模块id集
     */
    @Column(name = "module_ids")
    private String moduleIds;

    private Integer state;

    /**
     * 描述
     */
    private String description;




}