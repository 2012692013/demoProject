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
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface ExcelExporter {
    /**
     * 表名
     * @return
     */
    String value() default "";

    /**
     * 工作表名 没有的时候默认工作表名
     * @return
     */
    String sheet() default "";

    /**
     * 描述
     * @return
     */
    String desc() default "";
}
