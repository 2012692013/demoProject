package com.qyh.demo.mapper;

import com.qyh.demo.entity.Module;
import com.qyh.demo.vo.ModuleVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ModuleMapper extends Mapper<Module> {
    /**
     * 后台查询模块列表
     * @param whereMap
     * @return
     * 2018年7月11日
     * @author qiuyuehao
     */
    List<Map<String, Object>> findListByLimitPageWeb(Map<String, Object> whereMap);

    /**
     * 查询树形结构模块列表
     * @param whereMap
     * @return
     * 2018年7月11日
     * @author qiuyuehao
     */
    List<ModuleVo> findModuleListByLimit(Map<String, Object> whereMap);

    /**
     * 后台查询模块详情
     * @param id
     * @return
     * 2018年7月11日
     * @author qiuyuehao
     */
    Map<String, Object> findOneByLimitWeb(String id);

    /**
     * 条件统计模块名称
     * @param whereMap
     * @return
     * 2018年7月11日
     * @author qiuyuehao
     */
    String countModuleNames(Map<String, Object> whereMap);

    /**
     * 查询模块id集
     * @param whereMap2
     * @return
     * 2018年7月11日
     * @author qiuyuehao
     */
    String findModuleIds(Map<String, Object> whereMap2);
}