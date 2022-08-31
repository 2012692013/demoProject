package com.qyh.demo.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * map工具
 * @author qiuyuehao
 *
 */
public class MapUtil {
	/**
	 * 得到驼峰的KEY
	 * @param sourceList
	 * @return
	 */
	public static List<Map<String,Object>> getHumpKey(List<Map<String,Object>> sourceList){
		if(sourceList != null){
			List<Map<String,Object>> targetList = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> sourceMap : sourceList){
				targetList.add(mapKeyUnderlineToHump(sourceMap));
			}
			return targetList;
		}
		return null;
	}
	
	/**
	 * 把map的key由下划线转成驼峰
	 * @param sourceMap
	 * @return
	 */
	public static Map<String,Object> mapKeyUnderlineToHump(Map<String,Object> sourceMap){
		if(sourceMap != null){
			Map<String,Object> targetMap = new HashMap<String, Object>();
			
			for(Map.Entry<String,Object> source : sourceMap.entrySet()){
				String key = source.getKey();
				if(key.indexOf("_") != -1){
					String[] keyArry = key.split("_");
					
					StringBuffer sb = new StringBuffer();
					sb.append(keyArry[0]);
					for(int i=1;i<keyArry.length;i++){
						sb.append(captureString(keyArry[i]));
					}
					key = sb.toString();
				}
				targetMap.put(key, source.getValue());
			}
			
			return targetMap;
		}
		return null;
	}
	/**
	 * 首字母大写
	 * @param name
	 * @return
	 */
	public static String captureString(String string) {
		char[] cs=string.toCharArray();
		cs[0]-=32;
		return String.valueOf(cs);
	}
	
}
