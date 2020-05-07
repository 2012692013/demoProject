/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/8/24 17:10:06                           */
/*==============================================================*/


drop table if exists area;

drop table if exists feedback;

drop table if exists message;

drop table if exists module;

drop table if exists role;

drop table if exists sys_code;

drop table if exists sys_exception;

drop table if exists sys_user;

/*==============================================================*/
/* Table: area                                                  */
/*==============================================================*/
create table area
(
   area_id              varchar(32) not null comment '区域id',
   area_code            varchar(20) comment '区域编码',
   area_name            varchar(20) comment '区域名称',
   level                integer comment '区域等级',
   city_code            varchar(20) comment '城市编码',
   parent_id            varchar(32) comment '父id',
   lat                  varchar(255) comment '纬度',
   lon                  varchar(255) comment '经度',
   primary key (area_id)
);

alter table area comment '区域';

/*==============================================================*/
/* Table: feedback                                              */
/*==============================================================*/
create table feedback
(
   id                   varchar(32) not null,
   content              varchar(500) comment '反馈内容',
   imgs                 varchar(2000) comment '反馈图片集',
   state                integer,
   create_man           varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_man           varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   version              bigint comment '版本时间戳',
   del_flag             int comment '删除标记（0：否，1：是）',
   primary key (id)
);

/*==============================================================*/
/* Table: message                                               */
/*==============================================================*/
create table message
(
   id                   varchar(32) not null comment 'id',
   user_id              varchar(32),
   type                 varchar(10) comment '//todo',
   title                varchar(50) comment '标题',
   content              varchar(500) comment '消息内容',
   fk_id                varchar(32) comment '跳转id',
   skip_way             varchar(32) comment '//todo',
   is_read              integer comment '1已读 0未读',
   create_man           varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_man           varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   version              bigint comment '版本时间戳',
   del_flag             int default 0 comment '删除标记（0：否，1：是）',
   primary key (id)
);

alter table message comment '消息';

/*==============================================================*/
/* Table: module                                                */
/*==============================================================*/
create table module
(
   id                   varchar(32) not null comment 'id',
   pid                  varchar(32) comment '父级ID',
   icon                 varchar(20) comment '图标',
   module_name          varchar(20) comment '模块名称',
   request_url          varchar(100) comment '请求路径',
   description          varchar(200) comment '描述',
   number               varchar(5) comment '序号',
   state                int default 0 comment '状态',
   create_man           varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_man           varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   version              bigint comment '版本时间戳',
   del_flag             int default 0 comment '删除标记（0：否，1：是）',
   primary key (id)
);

alter table module comment '模块表';

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   varchar(32) not null comment 'id',
   role_name            varchar(50) comment '角色名',
   module_ids           longtext comment '模块集合',
   state                int default 0 comment '状态',
   description          varchar(200) comment '描述',
   create_man           varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_man           varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   version              bigint comment '版本时间戳',
   del_flag             int default 0 comment '删除标记（0：否，1：是）',
   primary key (id)
);

alter table role comment '角色表';

/*==============================================================*/
/* Table: sys_code                                              */
/*==============================================================*/
create table sys_code
(
   id                   varchar(32) not null comment 'id',
   code_level           varchar(50) comment 'sys:系统级别 bis:业务级别',
   type                 varchar(10) comment '类型',
   code_key             varchar(500) comment '键',
   value_type           varchar(20) comment 'txt:文本 imgContent:图文 file:文件',
   code_value           longtext comment '值',
   state                int comment '状态',
   description          varchar(500) comment '说明',
   create_man           varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_man           varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   version              bigint comment '版本时间戳',
   del_flag             int default 0 comment '删除标记（0：否，1：是）',
   primary key (id)
);

alter table sys_code comment '系统参数';

/*==============================================================*/
/* Table: sys_exception                                         */
/*==============================================================*/
create table sys_exception
(
   id                   varchar(32) not null,
   name                 varchar(50) comment '异常名称',
   info                 text comment '异常信息',
   ip                   varchar(200) comment 'ip',
   way                  varchar(20) comment '请求方式',
   method               varchar(100) comment '方法',
   param                varchar(500) comment '请求参数',
   state                int default 0 comment '状态',
   create_man           varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_man           varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   version              bigint comment '版本时间戳',
   del_flag             int default 0 comment '删除标记（0：否，1：是）',
   primary key (id)
);

/*==============================================================*/
/* Table: sys_user                                              */
/*==============================================================*/
create table sys_user
(
   id                   varchar(32) not null comment 'id',
   account              varchar(20) comment '登录账号',
   password             varchar(32) comment '登录密码',
   token                varchar(32) comment '登录令牌',
   role_ids             varchar(255) comment '角色ID集',
   role_name            varchar(100) comment '角色名称',
   user_type            varchar(20) comment '用户类型 （用户:user 管理员:admin，administrator:超级管理员）',
   nickname             varchar(500) comment '昵称',
   head_img             varchar(500) comment '头像',
   state                int default 0 comment '状态',
   create_man           varchar(32) comment '创建人',
   create_time          datetime comment '创建时间',
   update_man           varchar(32) comment '更新人',
   update_time          datetime comment '更新时间',
   version              bigint comment '版本时间戳',
   del_flag             int default 0 comment '删除标记（0：否，1：是）',
   primary key (id)
);

alter table sys_user comment '用户表';

