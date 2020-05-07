package com.qyh.demo.base.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

/**
 * <p style="color:blue">  map和bean互转工具类</p>
 * @author moon
 *
 */
public class BeanUtil {

    static{
//        ConvertUtils.register( new DateTimeConverter(), java.util.Date.class);
    }
    /**
     * Map --> Bean 2: 利用org.apache.commons.beanutils 工具类实现 Map --> Bean  注意：map的Key和Bean的属性区分大小写
     * @param map map集合
     * @param obj 实体对象
     */
    public static void transMapToBean(Map<String, Object> map, Object obj) {
        if (map == null || obj == null) {
            return;
        }
        try {
            BeanUtils.populate(obj, map);
        } catch (Exception e) {
            System.out.println("map转换bean失败 " + e);
        }
    }
    /**
     * Bean -->Map  2: 利用org.apache.commons.beanutils 工具类实现 Bean -->   Map
     * @param obj 要转换的实体
     * @return
     */
    public static Map<String, String>  transBeanToMap( Object obj) {
        Map<String, String> map=null;
        if ( obj == null) {
            return map;
        }
        try {
            map=  BeanUtils.describe(obj);
        } catch (Exception e) {
            System.out.println("bean转换map失败 " + e);
        }
        return map;
    }
    /**
     * 将对象转换成Map<String, Object>格式
     *
     * @param obj
     * @return
     */
//    public static Map<String, Object> getNamValMap(Object obj, boolean isSort) {
    public static Map<String, Object> getObjValMap(Object obj) {
        Map<String, Object> map = null;
//        if(isSort) {
//            map = new TreeMap<String, Object>();
//        }else{
        map = new HashMap<String, Object>();
//        }
        Field[] fieldArr = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fieldArr) {
                field.setAccessible(true);
//                if (field.get(obj) != null && !"".equals(field.get(obj).toString())) {
                map.put(field.getName(), field.get(obj));
//                }
            }
        } catch (IllegalAccessException e) {

        }
        return map;
    }
    /**
     * 根据页面表单生成实体类
     * 注意：如果表单传下的是个数组将不会封装进实体类中
     * @param request
     * @param entity 实体
     * @param exclude 需要排除的列
     */
    public static void getEntityByForm(HttpServletRequest request,Object entity,List<String> exclude){
    	Map<String, String[]> formMap = request.getParameterMap();
		Map<String, Object> map = new HashMap<String, Object>();
		if(formMap.size() > 0){
			Set<String> keySet = formMap.keySet();
			Iterator it = keySet.iterator();
			
			while (it.hasNext()) {
				String key = (String) it.next();
				if(formMap.get(key).length == 1){
					if(exclude != null && exclude.contains(key)){
						continue;
					}
					map.put(key, formMap.get(key)[0]);
				}
			}

			transMapToBean(map, entity);
		}
    }
    
    /**
     * 复制实体
     * @param target 目标
     * @param source 来源
     */
    public static void copyProperties(Object target,Object source){
    	try {
			BeanUtils.copyProperties(target, source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("Name", "测试一");
		map.put("description", "说明");

		/*FwdjModule fm = new FwdjModule();
		transMapToBean(map,fm);
		System.out.println(fm.getName()+"@"+fm.getDescription());*/
	}
}
