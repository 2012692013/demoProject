package com.qyh.demo.excel;
import java.lang.annotation.*;

/**
 * 特殊性Excel文件头读取/请使用ccys/../Util同包下的工具
 * @author mars_q
 * @date 2020/7/6 11:29
 * create by 2012692013@qq.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface ExcelImporter {
    /**
     * 表名
     * @return
     */
    String value() default "";

    /**
     * 工作表名 没有的时候默认取sheet(0)
     * @return
     */
    String sheetName() default "";

    /**
     * 标题行
     * @return
     */
    int titleRow() default 0;

    /**
     * 起始行
     * @return
     */
    int startRow() default 1;

    /**
     * 描述
     * @return
     */
    String desc() default "";
}



