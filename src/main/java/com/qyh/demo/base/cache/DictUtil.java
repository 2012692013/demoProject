package com.qyh.demo.base.cache;

import com.qyh.demo.base.util.JedisUtil;
import com.qyh.demo.enums.JedisIndex;

import java.util.HashMap;
import java.util.Map;

public class DictUtil {
	
	
//	private static final String CACHE_NAME = "dictCache";

	private static final int JEDIS_INDEX = JedisIndex.DICT;

	/**
	 * 获取字典详情值
	 * @author hx
	 * 2018年6月15日
	 * @param dictValue
	 * @param dataName
	 * @return
	 */
	public static String getDictData(String dictValue, String dataName){
		/*Element e = Kit.getElement(CACHE_NAME, dictValue);
		if(e==null){
			return null;
		}*/
		Object obj = JedisUtil.getJedisUtil().getObject(dictValue,JEDIS_INDEX);
		if(obj == null){
			return null;
		}
		Map<String,String> dictMap = (Map<String, String>) obj;
		return dictMap.get(dataName);
	}
	
	
	/**
	 * 更新字典
	 * @author hx
	 * 2018年6月15日
	 * @param option update更新名称      delete删除字典
	 * @param dictValue
	 * @param oldValue
	 * @throws CloneNotSupportedException 
	 */
	public static void updateDict(String option, String dictValue, String oldValue){
		/*if("update".equals(option)){
			Element e = Kit.getElement(CACHE_NAME, oldValue);
			if(e==null){
				return;
			}else{
				try {
					Element element = (Element) e.clone();
					Kit.setElement(CACHE_NAME, dictValue, element.getObjectValue());
					Kit.removeElement(oldValue, CACHE_NAME);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}else if("delete".equals(option)){
			Kit.removeElement(dictValue, CACHE_NAME);
		}*/
		if("update".equals(option)){
			Object obj = JedisUtil.getJedisUtil().getObject(oldValue,JEDIS_INDEX);
			if(obj == null){
				return;
			}else{
				JedisUtil.getJedisUtil().setObj(dictValue,obj,JEDIS_INDEX);
				JedisUtil.getJedisUtil().del(oldValue, JEDIS_INDEX);
			}
		}else if("delete".equals(option)){
			JedisUtil.getJedisUtil().del(dictValue, JEDIS_INDEX);
		}
	}
	
	/**
	 * 更新字典详情
	 * @author hx
	 * 2018年6月15日
	 * @param option save添加		delete删除
	 * @param dictValue
	 * @param dataName
	 * @param dataValue
	 */
	public static void updateDictData(String option, String dictValue, String dataName, String dataValue){
//		Element e = Kit.getElement(CACHE_NAME, dictValue);
		Object obj = JedisUtil.getJedisUtil().getObject(dictValue, JEDIS_INDEX);
		Map<String,String> map = new HashMap<String,String>();
		if(obj!=null){
			map = (Map<String, String>) obj;
		}
		
		
		switch (option) {
			case "save":
				map.put(dataName, dataValue);
				if(obj==null){
//					Kit.setElement(CACHE_NAME, dataValue, map);
					JedisUtil.getJedisUtil().setObj(dataValue, map, JEDIS_INDEX);
				}
				break;
				
				
			case "delete":
				map.remove(dataName);
				JedisUtil.getJedisUtil().setObj(dataValue, map, JEDIS_INDEX);
				break;
				
				
			default:
				break;
		}
	}
	
}
