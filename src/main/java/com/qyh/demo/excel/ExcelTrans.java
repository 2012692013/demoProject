package com.qyh.demo.excel;

import java.lang.annotation.*;

/**
 * Excel文件->bean/bean->文件 值相互转换/请使用ccys/../Util同包下的工具
 * @author mars_q
 * @date 2020/7/6 11:29
 * create by 2012692013@qq.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface ExcelTrans {

    /**
     * 当前类的值
     * @return
     */
    String bean() default "";

    /**
     * 描述
     * @return
     */
    String excel() default "";
}

