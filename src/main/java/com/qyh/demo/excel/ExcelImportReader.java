package com.qyh.demo.excel;

/**
 * EXCEL读取数据处理接口
 * @author mars_q
 * @Description
 * @e-mail: 2012692013@qq.com
 * @date 2020/8/13 001314:23
 */
public interface ExcelImportReader {
    /**
     * 实现此方法以在读取过程中对instance进行数据处理
     * 如果实体类内进行了autowired,那么请从spring上下文中获取
     * @param o
     * @return
     */
    Object reconstruct(Object o);
}

