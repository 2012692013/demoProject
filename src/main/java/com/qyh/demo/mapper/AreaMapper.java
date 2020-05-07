package com.qyh.demo.mapper;

import com.qyh.demo.entity.Area;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created with IDEA
 * author:huxi
 * Date:2019/3/22
 * Time:4:38 PM
 */
public interface AreaMapper extends Mapper<Area> {

    /**
     * 查询所有区域
     * @return
     */
    @Select("select t1.*,t2.area_name as provinceName from area t1 left join area t2 on t1.parent_id = t2.area_id order by t1.area_id")
    List<Area> findList();
}
