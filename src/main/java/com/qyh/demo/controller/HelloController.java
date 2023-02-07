package com.qyh.demo.controller;

import com.qyh.demo.base.cache.SysCodeUtil;
import com.qyh.demo.base.config.init.InitBean;
import com.qyh.demo.base.util.JedisUtil;
import com.qyh.demo.base.util.Kit;
import com.qyh.demo.base.util.bucket.TouristApi;
import com.qyh.demo.entity.SysCode;
import com.qyh.demo.entity.SysUser;
import com.qyh.demo.mapper.SysUserMapper;
import com.qyh.demo.service.UserService;
import com.qyh.demo.vo.Result;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.qyh.demo.repository.UserRepository;

@Api(tags="这是controller的标题")
@PropertySource("classpath:source.properties")
@RestController
@RequestMapping(value = "hello")
@TouristApi
public class HelloController {

	@Value("${tag.name}")
	private String name;
	
	@Value("${tag.price}")
	private String price;
	
	@Value("${tag.sex}")
	private String sex;
	
	@Value("${com.test.value}")
	private String rs;
	
	@Autowired
	private InitBean bean;
	
	@Autowired
	private SysUserMapper userMapper;
	
	@Autowired
	private UserService userSer;

//	@Autowired
//	private RedisService redisSer;
	
//	@Autowired
//	private UserRepository userRep;
	
	private static String type = "administrator";

	@GetMapping("test1")
	@TouristApi
	public String test1(@RequestParam Integer i){
		return i.toString();
	}

	@TouristApi()//游客模式注解
	@ApiOperation(value="get请求", notes="测试")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "string",paramType = "path"),
		@ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "string",paramType="path")
	})
	@RequestMapping(value="hello",method=RequestMethod.GET)
	@Transactional
	public String hello(){
		System.out.println(rs);
		System.out.println("配置:"+bean.getName()+";"+bean.getType());
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userType", type);
		if("administrator".equals(type)){
			type="member";
		}else{
			type="administrator";
		}
		
		List<SysUser> list = userMapper.selectAll();
		for(SysUser u : list){
			System.out.println(u.getUserType());
		}
		
		SysUser user = new SysUser();
		user.setUserType("member");
		list = userMapper.select(user);
		System.out.println(list.size());
		
//		userSer.findListMap();
		
		
//		list = userRep.findAllUserList();
//		for(SysUser u : list){
//			System.out.println(u.getAccount());
//			System.out.println(u.getUserType());
//		}
//		
//		list = userRep.findByUserType("member");
//		for(SysUser u : list){
//			System.out.println(u.getAccount());
//		}
		
//		Page<SysUser> page = userRep.findByUserType("member",new PageRequest(0,2,new Sort(Sort.Direction.DESC,"createTime")));
//		list = page.getContent();
		for(SysUser u : list){
			System.out.println(u.getAccount());
			System.out.println(u.getCreateTime());
		}
		
		SysUser user1 = new SysUser();
		user1.setAccount("123123");
		userSer.saveEntity(user1);
		System.out.println(list.get(11));
		return name+price+sex;
	}


	@TouristApi(tourist = false)
	@ApiOperation(value="post请求", notes="使用post请求")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "string",paramType = "path"),
		@ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "string",paramType="path")
	})
	@RequestMapping(value="postHello",method=RequestMethod.POST)
	public Result postHello(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("msg", "备注");
		Result r = new Result();
		
		PageHelper.startPage(0, 2,false);
		List<SysUser> userList = userSer.findListByLimit(SysUser.class, null, null);
		r.setData(userList);
		
		return r;
	}
	
	@RequestMapping(value="test",method=RequestMethod.GET)
	public String test(){
		SysUser user = new SysUser();
		user.preInsert(null);
		userSer.saveEntity(user);
		//user.preUpdate(request);
		System.out.println(user.toString());
		user.setAccount("110");
		return user.toString();
	}


	@RequestMapping(value="test",method=RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "string",paramType = "path"),
			@ApiImplicitParam(name = "account", value = "用户实体user", required = true, dataType = "string",paramType="path")
	})
	public String test(SysUser sysUser){
		return sysUser.toString();
	}
	
	@RequestMapping(value="getByTimestamp",method=RequestMethod.GET)
	public SysUser getByTimestamp(HttpServletRequest request){
		SysUser u = new SysUser();
		u.setVersion(1528789003625l);
		u = userMapper.selectOne(u);
		u.setAccount("110");
		Example e = new Example(SysUser.class);
		Criteria c = e.createCriteria();
		c.andEqualTo("id", u.getId());
		c.andEqualTo("version",u.getVersion());
		u.setVersion(System.currentTimeMillis());
//		userMapper.updateByExampleSelective(u, e);
		System.out.println(Kit.getCurLoginUser(request));
		return u;
	}





	@PostMapping("del")
	public void delJedis(String key,int index){
		JedisUtil.getJedisUtil().del(key,index);
	}

	@GetMapping("getCode")
	@TouristApi
	@ApiOperation("系统参数")
	public void getCode(String key, String type){
		SysCode code = SysCodeUtil.getCode(type, key);
		System.out.println(code.getCodeValue());
	}


	@GetMapping("testNginx")
	public String testNginx(){
		System.out.println("9000");
		return "我是9000";
	}

	public static void main(String[] args) {

	}
}
