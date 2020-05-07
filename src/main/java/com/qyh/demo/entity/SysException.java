package com.qyh.demo.entity;

import com.qyh.demo.base.BaseEntity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_exception")
@Data
public class SysException extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String ip;

    private String way;

    private String info;

    private String method;

    private String param;

    private Integer state;
}