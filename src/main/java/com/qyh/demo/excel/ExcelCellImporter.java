package com.qyh.demo.excel;

import java.lang.annotation.*;

/**
 * 特殊性Excel文件列读取/请使用ccys/../Util同包下的工具
 * @author mars_q
 * @date 2020/7/6 11:29
 * create by 2012692013@qq.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface ExcelCellImporter {

    /**
     * 列名称
     * @return
     */
    String value() default "";

    /**
     * 描述
     * @return
     */
    String desc() default "";

    /**
     * 是否必须
     * @return
     */
    boolean required() default true;

    /**
     * 需要转换解释的值
     * @return
     */
    ExcelTrans[] trans() default {};
}

