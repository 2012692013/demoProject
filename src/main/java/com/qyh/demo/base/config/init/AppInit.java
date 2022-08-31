package com.qyh.demo.base.config.init;

import com.qyh.demo.base.util.JedisUtil;
import com.qyh.demo.base.util.SerializationUtil;
import com.qyh.demo.base.util.bucket.QSwaggerContext;
import com.qyh.demo.base.util.bucket.TouristUtil;
import com.qyh.demo.entity.Area;
import com.qyh.demo.entity.SysCode;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.enums.JedisIndex;
import com.qyh.demo.mapper.AreaMapper;
import com.qyh.demo.mapper.SysCodeMapper;
import com.qyh.demo.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.*;

/**
 * 项目启动初始化
 * @author qiuyuehao
 * 2018年6月12日
 */
@Configuration
@Slf4j
public class AppInit implements CommandLineRunner{

	@Autowired
	private SysUserMapper userMapper;
	
	@Autowired
	private SysCodeMapper codeMapper;
	

	@Autowired
	private AreaMapper areaMapper;

	
	@Override
	@Order(value = 1)
	public void run(String... arg0) throws Exception {


		Jedis jedis = JedisUtil.getJedis();
		//初始化用户
		initUser(jedis);
		
		//初始化系统参数
		initSysCode(jedis);
		
		//初始化区域
		initArea(jedis);

		jedis.close();

		//加载游客模式
		initTouristApi();

		//参数限制
		setContext();
	}
	
	/**
	 * 初始化用户
	 * @author qiuyuehao
	 * 2018年6月14日
	 */
	private void initUser(Jedis jedis){
		jedis.select(JedisIndex.USER);
		log.info("--------------初始化用户缓存---------------");
		Example example = new Example(SysUser.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("state", 1);
		c.andIsNotNull("token");
		List<SysUser> userList = userMapper.selectByExample(example);
		
		if(userList.size()>0){
			for(SysUser user:userList){
				jedis.set(user.getToken().getBytes(), SerializationUtil.serialize(user));
//				Kit.setElement("tokenCache", user.getToken(), user);
			}
		}
		log.info("===========初始化用户缓存完成,总数:"+userList.size()+"=========");
	}

	/**
	 * 初始化系统参数
	 * @author qiuyuehao
	 * 2018年6月14日
	 */
	private void initSysCode(Jedis jedis){
		jedis.select(JedisIndex.CODE);
		log.info("--------------开始初始化系统参数------------");
		//查询出所有的系统参数
		Example e = new Example(SysCode.class);
		Criteria c = e.createCriteria();
		c.andEqualTo("state", 1);
		e.orderBy("type");
		List<SysCode> codeList = codeMapper.selectByExample(e);
		
		if(codeList.isEmpty()){
			log.info("---------无数据---------");
			return;
		}
		
		//将参数类型分类 放入map
		Map<String,SysCode> map = new HashMap<String,SysCode>();
		String type = codeList.get(0).getType();
		int i = 0;
		for(SysCode code:codeList){
			if(!type.equals(code.getType())){
				//添加进参数缓存
				jedis.set(type.getBytes(),SerializationUtil.serialize(map));
//				Kit.setElement("sysCodeCache", type, map);
				map = new HashMap<String, SysCode>();
				type = code.getType();
				i++;
			}
			map.put(code.getCodeKey(), code);
		}

		if(!map.isEmpty()){
			jedis.set(type.getBytes(),SerializationUtil.serialize(map));
//			Kit.setElement("sysCodeCache", type, map);
		}
		
		log.info("==========系统参数初始化完毕，总计:"+codeList.size()+"条，"+(i+1)+"组==============");
		
	}


	private void initArea(Jedis jedis){
		log.info("==========开始初始化区域==============");
		
		jedis.select(JedisIndex.AREA);

		List<Area> areaList = areaMapper.findList();

		Map<String,Object> allProvinceMap = new LinkedHashMap<>();

		for (Area a:areaList){
			jedis.set(String.valueOf(a.getAreaId()).getBytes(),SerializationUtil.serialize(a));

			if(a.getLevel()==2){
				Map<String,Area> provinceMap = new LinkedHashMap<>();

				List<Area> list = new ArrayList<>();
				Map<String,Object> map = new HashMap<>();

				if(allProvinceMap.get(a.getParentId())==null){
					map.put("id",a.getParentId());
					map.put("name",a.getProvinceName());
				}else{
					map = (Map<String, Object>) allProvinceMap.get(String.valueOf(a.getParentId()));
					list = (List<Area>) map.get("list");
				}
				list.add(a);
				map.put("list",list);
				allProvinceMap.put(a.getParentId(),map);

			}
		}
		List<Map<String,Object>> pList = new ArrayList<>();

		Set<Map.Entry<String, Object>> l = allProvinceMap.entrySet();
		for(Map.Entry e : l){
			pList.add((Map<String, Object>) e.getValue());
		}

		jedis.set("all".getBytes(),SerializationUtil.serialize(pList));

		log.info("==========区域初始化完毕，共计："+areaList.size()+"个城市==============");
	}


	private void initTouristApi(){
		Map tourist = TouristUtil.tourist();
		Set set = tourist.keySet();
		for (Object key:set
		) {
			log.info(String.valueOf(key));
			log.info(String.valueOf(tourist.get(key)));
		}
	}

	private void setContext() throws ClassNotFoundException {
		log.info("============正在加载必要上下文============");
		QSwaggerContext.setContext();
		Map<String, List<String>> contentMap = QSwaggerContext.CONTENT_MAP;
		for (String key :
				contentMap.keySet()) {
			log.info(key);
			log.info(contentMap.get(key).toString());
		}
	}
}
