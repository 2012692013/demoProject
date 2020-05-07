package com.qyh.demo.entity;

import com.qyh.demo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Module extends BaseEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 父级ID
     */
    private String pid;

    /**
     * 图标
     */
    private String icon;

    /**
     * 模块名称
     */
    @Column(name = "module_name")
    private String moduleName;

    /**
     * 请求路径
     */
    @Column(name = "request_url")
    private String requestUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 序号
     */
    private Integer number;

    private Integer state;




}