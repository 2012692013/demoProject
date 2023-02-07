package com.qyh.demo.excel;

import java.lang.annotation.*;

/**
 * excel文件导出/请使用ccys/../Util同包下的工具
 * @author mars_q
 * @date 2020/7/10 11:29
 * create by 2012692013@qq.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface ExcelCellExporter {

    /**
     * 列名称
     * @return
     */
    String value() default "";

    /**
     * 排序
     * @return
     */
    int order() default 0;

    /**
     * 描述
     * @return
     */
    String desc() default "";

    /**
     * 需要转换解释的值
     * @return
     */
    ExcelTrans[] trans() default {};
}

