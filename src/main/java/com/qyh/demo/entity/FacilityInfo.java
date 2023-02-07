package com.qyh.demo.entity;

import com.qyh.demo.excel.ExcelCellImporter;
import com.qyh.demo.excel.ExcelImporter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Qiu_yh
 * @date 2022/9/6 16:04
 */
@Data
@ExcelImporter
public class FacilityInfo {
    @ExcelCellImporter(value = "")
    private Integer id;
    @ExcelCellImporter(value = "")
    private Integer devc_id;
    @ExcelCellImporter(value = "")
    private Integer devc_name;
    @ExcelCellImporter(value = "")
    private Integer rid;
    @ExcelCellImporter(value = "")
    private Integer lng;
    @ExcelCellImporter(value = "")
    private Integer lat;
    @ExcelCellImporter(value = "")
    private Integer road_name;
    @ExcelCellImporter(value = "")
    private Integer pile_no;
    @ExcelCellImporter(value = "")
    private Integer sub_facility_type_no;
    @ExcelCellImporter(value = "")
    private Integer gmt_create;
    @ExcelCellImporter(value = "")
    private Integer facility_type_no;
    @ExcelCellImporter(value = "")
    private Integer adcode;
    @ExcelCellImporter(value = "")
    private Integer facility_angle;
    @ExcelCellImporter(value = "")
    private Integer bearing;
    @ExcelCellImporter(value = "")
    private Integer alt;
    @ExcelCellImporter(value = "")
    private Integer pair;
    @ExcelCellImporter(value = "")
    private Integer model_file_name;
//    private Integer bearing（偏转角）;

}
