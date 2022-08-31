package com.qyh.demo.base.util.bucket;

import java.lang.annotation.*;

/**
 * @author qiuyuehao
 * @date 2019/5/31 13:00
 * create by 2012692013@qq.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface TouristApi {
    boolean tourist() default true;
}
