package com.qyh.demo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created with IDEA
 * author:qiuyuehao
 * Date:2019/3/22
 * Time:4:24 PM
 */
@Data
public class Area implements Serializable {

    /**
     * 区域id
     */
    @Id
    @Column(name = "area_id")
    private String areaId;

    /**
     * 区域编号
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 区域名称
     */
    @Column(name = "areaName")
    private String areaName;

    /**
     * 等级 1：省 2：市
     */
    private int level;

    /**
     * 城市编码
     */
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 经度
     */
    private String lon;

    /**
     * 上级id
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 省名称
     */
    @Transient
    private String provinceName;
}
