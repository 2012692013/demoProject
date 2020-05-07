package com.qyh.demo.dto;

import lombok.Data;

/**
 * Created with IDEA
 * author:huxi
 * Date:2019/7/4
 * Time:5:02 PM
 */
@Data
public class PageDto {
    private Integer pageNum;
    private Integer pageSize;
    private Boolean countSql = true;
    private String orderBy = null;
    private Boolean reasonable = false;
    private Boolean pageSizeZero = false;
}
