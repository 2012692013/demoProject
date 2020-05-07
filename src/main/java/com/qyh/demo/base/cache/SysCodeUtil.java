package com.qyh.demo.base.cache;

import com.qyh.demo.base.util.JedisUtil;
import com.qyh.demo.entity.SysCode;
import com.qyh.demo.enums.JedisIndex;

import java.util.HashMap;
import java.util.Map;

public class SysCodeUtil {

//	private static final String CACHE_NAME = "sysCodeCache";
	private static final int JEDIS_INDEX = JedisIndex.CODE;
	
	/**
	 * 通过类型，键获取系统参数
	 * @author hx
	 * 2018年6月15日
	 * @param type 参数类型
	 * @param codeKey 参数键
	 * @return
	 */
	public static SysCode getCode(String type, String codeKey){
//		Element e = Kit.getElement(CACHE_NAME, type);
		Object obj = JedisUtil.getJedisUtil().getObject(type, JEDIS_INDEX);
		if(obj==null){
			return null;
		}
		
		Map<String,SysCode> map = (Map<String, SysCode>) obj;
		return map.get(codeKey);
	}
	
	/**
	 * 更新系统参数
	 * @author hx
	 * 2018年6月15日
	 * @param option save添加    update更新    delete删除
	 * @param code 系统参数实体
	 * @param oldKey 旧数据参数键
	 * @param oldType 旧数据参数类型
	 */
	public static void updateCode(String option, SysCode code, String oldKey, String oldType){
//		Element e = Kit.getElement(CACHE_NAME, code.getType());
		Object obj = JedisUtil.getJedisUtil().getObject(code.getType(), JEDIS_INDEX);
		
		Map<String,SysCode> map = new HashMap<String,SysCode>();
		if(obj!=null){
			map = (Map<String, SysCode>) obj;
		}
		
		switch (option) {
			case "save"://添加
				map.put(code.getCodeKey(), code);
				if(obj==null){
					JedisUtil.getJedisUtil().setObj(code.getType(), map, JEDIS_INDEX);
//					Kit.setElement(CACHE_NAME, code.getType(), map);
				}
				break;

			case "update"://更新
				//先删除旧数据
//				Element oldE = Kit.getElement(CACHE_NAME, oldType);
				if(oldType.equals(code.getType())){
					map.remove(oldKey);
				}else{
					Object oldO = JedisUtil.getJedisUtil().getObject(oldType, JEDIS_INDEX);
					if(oldO!=null){
						Map<String,SysCode> codeMap = (Map<String, SysCode>) oldO;
						codeMap.remove(oldKey);
						JedisUtil.getJedisUtil().setObj(oldType, codeMap, JEDIS_INDEX);
					}
				}
				//添加新数据
				map.put(code.getCodeKey(), code);
				JedisUtil.getJedisUtil().setObj(code.getType(), map, JEDIS_INDEX);

				break;
				
			case "delete"://删除
				map.remove(code.getCodeKey());
				JedisUtil.getJedisUtil().setObj(code.getType(),map,JEDIS_INDEX);
				break;
				
			default:
				break;
		}
		
	}
	
}
