package com.qyh.demo.base.third.netease;

import com.qyh.demo.base.config.init.InitBean;
import com.qyh.demo.base.util.Kit;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


/** 网易云 即时通讯 常用方法工具类
* @Description:
* @CreateDate:     2019年02月19日 17:00:38
* @author qiuyuehao
*/
@Slf4j
public class HttpClient {
	
	@Autowired
	private InitBean bean;//初始化配置类
	

	
	/**
	 * @description 注册网易云IM用户
	 * @throws Exception
	 * @param paramMap
	 * 2018年11月1日
	 * @author qiuyuehao
	 */
	public static Map<String,String> registerNetEaseUser(Map<String,String> paramMap) throws Exception {
		Map<String,String> resultMap = new HashMap<String,String>();
		String url = "https://api.netease.im/nimserver/user/create.action";
		//网易云接口返回结果 == {"code":200,"info":{"token":"test2","accid":"test2","name":"test2"}}
		//请求头
		Map<String,String> headerMap = new HashMap<String,String>();
		
		String postNetEase = HttpClient.postNetEase(url, paramMap, headerMap);
		if(StringUtils.isNotEmpty(postNetEase)) {
			JSONObject json = JSONObject.fromObject(postNetEase);
			String code = json.get("code").toString();
			Object object = json.get("info");
			if("200".equals(code) && object != null) {
				resultMap.put("state", "1");
				resultMap.put("msg", "注册成功");
				log.info("网易云账号注册成功账号：accid="+paramMap.get("accid")+"  密码==="+paramMap.get("token"));
			}else{
				resultMap.put("state", "0");
				resultMap.put("msg", json.getString("desc").toString());
				log.info("网易云账号注册失败：失败原因====" + json.getString("desc").toString());
			}
			
		}else{
			resultMap.put("state", "0");
			resultMap.put("msg", "网易云注册失败");
		}
		return resultMap;
	}
	
	/**
	 * @description 查询网易云用户  名片信息
	 * @param userIds  用户id基，多个用户英文逗号等
	 * @return map<String,String>  state:0 失败，1：成功，msg：描述，
	 * uinfos：用户信息  json字符数组[{"accid":"test3","name":"test3","gender":0}]
	 * @throws Exception
	 * 2018年11月1日
	 * @author qiuyuehao
	 */
	public static Map<String,String> getNetEaseUser(String userIds) throws Exception {
		String url = "https://api.netease.im/nimserver/user/getUinfos.action";
		Map<String,String> resultMap = new HashMap<String,String>();		
		Map<String,String> paramMap = new HashMap<String,String>();
		JSONArray paramList = new JSONArray();
		if(StringUtils.isEmpty(userIds)) {
			resultMap.put("state", "0");
			resultMap.put("msg", "网易云用户不存在");
			resultMap.put("uinfos", "");
			return resultMap;
		}
		for(String value : userIds.split(",")) {
			paramList.add(value);
		}
		paramMap.put("accids", paramList.toString());
		//请求头
		Map<String,String> headerMap = new HashMap<String,String>();
		String postNetEase = HttpClient.postNetEase(url, paramMap, headerMap);
		if(StringUtils.isNotEmpty(postNetEase)) {
			JSONObject json = JSONObject.fromObject(postNetEase);
			String code = json.get("code").toString();
			Object object = json.get("uinfos");
			if("200".equals(code) && object != null) {
				resultMap.put("state", "1");
				resultMap.put("msg", "获取成功");
				resultMap.put("uinfos", json.getString("uinfos"));
			}else{
				resultMap.put("state", "0");
				resultMap.put("msg", json.getString("desc").toString());
				resultMap.put("uinfos", "");
				
			}
			
		}else{
			resultMap.put("state", "0");
			resultMap.put("msg", "网易云用户不存在");
			resultMap.put("uinfos", "");
		}
		//网易云接口返回结果 == {"code":200,"uinfos":[{"accid":"test3","name":"test3","gender":0}]}
		return resultMap;
		
	}
	
	/**
	 * @description 网易云通信ID更新
	 * @param paramMap accid,token,name  用户id 必传
	 * @return state：0失败，1成功，msg：描述信息
	 * @throws Exception
	 * 2018年11月1日
	 * @author qiuyuehao
	 */
	public static Map<String,String> updateNetEaseUser(Map<String,String> paramMap) throws Exception {
		Map<String,String> resultMap = new HashMap<String,String>();
		String url = "https://api.netease.im/nimserver/user/update.action";
		//请求参数
		/*Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("accid", "test1");
		paramMap.put("token", "test1");
		paramMap.put("name", "test1");*/
		//请求头
		Map<String,String> headerMap = new HashMap<String,String>();
		String postNetEase = HttpClient.postNetEase(url, paramMap, headerMap);
		if(StringUtils.isNotEmpty(postNetEase)) {
			JSONObject json = JSONObject.fromObject(postNetEase);
			String code = json.get("code").toString();
			//Object object = json.get("info");
			if("200".equals(code)) {
				resultMap.put("state", "1");
				resultMap.put("msg", "更新网易云基本用户信息");
			}else{
				resultMap.put("state", "0");
				resultMap.put("msg", json.getString("desc").toString());
			}
			
		}else{
			resultMap.put("state", "0");
			resultMap.put("msg", "更新 网易云用户信息成功");
		}
		//网易云接口返回结果 == {"code":200}
		return resultMap;
	}
	
	
	/**
	 * @description 更新用户名片信息  paramMap accid,icon,name ... 用户id 必传
	 * 参考文档  https://dev.yunxin.163.com/docs/product/IM即时通讯/服务端API文档/网易云通信ID
	 * @param paramMap 参数封装
	 * @return state：0失败，1成功，msg：描述信息
	 * @date     2019年02月19日 16:59:54
	 * @author qiuyuehao
	 * @throws Exception
	 */
	public static Map<String,String> updateNetEaseUserInfo(Map<String,String> paramMap) throws Exception {
		String url = "https://api.netease.im/nimserver/user/updateUinfo.action";

		Map<String,String> resultMap = new HashMap<>();
		//请求参数 实例
		/*Map<String,String> resultMap = new HashMap<String,String>();
		paramMap.put("accid", "test3");
		paramMap.put("name", "test3");
		paramMap.put("icon", "图标");
		paramMap.put("sign", "签名");
		//paramMap.put("email", "邮件");
		//paramMap.put("birth", "生日");
		//paramMap.put("mobile", "电话");
		//paramMap.put("gender", "性别");//用户性别，0表示未知，1表示男，2女表示女，其它会报参数错误
		//paramMap.put("ex", "扩展字段封装成json串");
		*/
		//请求头
		Map<String,String> headerMap = new HashMap<String,String>();
		String postNetEase = HttpClient.postNetEase(url, paramMap, headerMap);
		//账号已注册   网易云接口返回结果 == {"desc":"already register","code":414}
		//注册成功返回     网易云接口返回结果 == {"code":200,"info":{"token":"test2","accid":"test2","name":"test2"}}
		if(StringUtils.isNotEmpty(postNetEase)) {
			JSONObject json = JSONObject.fromObject(postNetEase);
			String code = json.get("code").toString();
			if("200".equals(code) ) {
				resultMap.put("state", "1");
				resultMap.put("msg", "更新成功");
			}else{
				resultMap.put("state", "0");
				resultMap.put("msg", json.getString("desc").toString());
			}
			
		}else{
			resultMap.put("state", "0");
			resultMap.put("msg", "网易云更新失败");
		}
		//网易云接口返回结果 == {"desc":"邮件 not email","code":414}
		//网易云接口返回结果 == {"code":200}
		return resultMap;
	}

	/**
	 * @description 封装 网易云公共请求
	 * @param url		    接口地址
	 * @param paramMap		参数map
	 * @param headerMap		请求头map
	 * @date     2019年02月19日 16:59:54
	 * @author qiuyuehao
	 */
	public static String postNetEase(String url, Map<String,String> paramMap,Map<String,String> headerMap)throws Exception {
		@SuppressWarnings("resource")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		//String url = "https://api.netease.im/nimserver/user/create.action";
		HttpPost httpPost = new HttpPost(url);
//		String appKey = "a702f169cc14bbe8411647de8e103XXX";//可以放在配置文件中读取
//		String appSecret = "9210f9553XXX";
		ResourceBundle resource = ResourceBundle.getBundle("init.properties");//test为属性文件名，放在包com.mmq下，如果是放在src下，直接用init.properties即可
		String appKey = resource.getString("init.neteaseAppKey");
		String appSecret = resource.getString("init.neteaseAppSecret");
		String nonce =  Kit.getRadomNumber(5);
		String curTime = Kit.getUTCTimeStr();
		String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码
		// 设置请求的header
		httpPost.addHeader("AppKey", appKey);
		httpPost.addHeader("Nonce", nonce);
		httpPost.addHeader("CurTime", curTime);
		httpPost.addHeader("CheckSum", checkSum);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		if(headerMap != null && !headerMap.isEmpty()) {
			for(String key: headerMap.keySet()) {
				httpPost.addHeader(key, headerMap.get(key));
			}
		}
		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if(paramMap != null && !paramMap.isEmpty()) {
			for(String key: paramMap.keySet()) {
				nvps.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		// 执行请求
		HttpResponse response = httpClient.execute(httpPost);

		// 打印执行结果
		String resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		log.info("网易云接口返回结果 == "+ resultString);
		return resultString;

	}

}
