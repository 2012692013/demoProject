package com.qyh.demo.entity;

import com.qyh.demo.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Message extends BaseEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 类型
     */
    private String type;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 外联id
     */
    @Column(name = "fk_id")
    private String fkId;

    /**
     * 跳转方式
     */
    @Column(name = "skip_way")
    private String skipWay;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    private Integer isRead;

}