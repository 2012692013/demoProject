package com.qyh.demo.entity;

import com.qyh.demo.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "sys_code")
@Data
public class SysCode extends BaseEntity implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * sys:系统级别 bis:业务级别
     */
    @Column(name = "code_level")
    private String codeLevel;

    /**
     * 类型
     */
    private String type;

    /**
     * 键
     */
    @Column(name = "code_key")
    private String codeKey;

    /**
     * 值
     */
    @Column(name = "code_value")
    private String codeValue;


    /**
     * txt:文本 imgContent:图文 file:文件
     */
    @Column(name = "value_type")
    private String valueType;

    /**
     * 说明
     */
    private String description;

    private Integer state;


}