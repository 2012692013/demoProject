package com.qyh.demo.base.third.pay;

import com.qyh.demo.base.third.pay.wechat.message.Property;
import com.qyh.demo.base.third.pay.wechat.message.WxMessage;
import com.qyh.demo.base.third.pay.wechat.wechaPay.AssistUtil;
import com.qyh.demo.base.third.pay.wechat.wechaPay.Wechat;
import com.qyh.demo.base.util.HttpsGet;
import com.qyh.demo.base.util.HttpsPost;
import com.qyh.demo.base.util.Kit;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WechatUtil {



	/**
	 * 微信退款
	 * @param payWay	支付方式:app支付：app/公众号支付:js
	 * @param trade_no	交易单号
	 * @param countMoney 订单总金额
	 * @param money		退款金额
	 * @param batch_no	批次号
	 * @param path		证书地址
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> wechatRefund(String payWay,String trade_no,String countMoney,String money,String batch_no,String path) throws Exception{
		String appId = Wechat.APP_ID,mchId = Wechat.MCH_ID,apiKey = Wechat.API_KEY;
		if("js".equals(payWay)){
			appId = Wechat.JS_APP_ID;
			mchId = Wechat.JS_MCH_ID;
			apiKey = Wechat.JS_API_KEY;
		}

		return Wechat.refund(appId, mchId, apiKey, trade_no,countMoney,money,batch_no, path);
	}


	/**
	 * 得到微信回调传来的参数
	 * @param
	 * @return
	 * @throws IOException
	 */
	public static Map<String,String> getWechatParams(HttpServletRequest request) throws Exception{
		Map<String,String> params = new HashMap<String,String>();

		String inputLine;
		String notityXml = "";
		while ((inputLine = request.getReader().readLine()) != null) {
			notityXml += inputLine;
		}
		request.getReader().close();
		if(StringUtils.isNotEmpty(notityXml)){
			params = AssistUtil.parseXml(notityXml);
		}

		System.out.println("微信回调函数："+notityXml);
		return params;
	}

	/**
	 * 验证是否是微信发出的请求
	 * @param params
	 * @return
	 */
	public static boolean wechatVerify(Map<String,String> params){
		String sign = AssistUtil.getSignCommon("UTF-8", params, Wechat.API_KEY);
		return sign.equals(params.get("sign"));
	}

	/**
	 * 给微信回调返回状态
	 * @param response
	 * @param isOk
	 * @throws Exception
	 */
	public static void returnState(HttpServletResponse response,boolean isOk) throws Exception{
		if(isOk){
			response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
		}else{
			response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[DEFEATED]]></return_msg></xml>");
		}
	}


	/*****************************微信公众号***************************/
	/**
	 * 根据CODE获取相应信息
	 * @param code
	 * @return
	 * { "access_token":"ACCESS_TOKEN",  	网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	"expires_in":7200,   				access_token接口调用凭证超时时间，单位（秒）
	"refresh_token":"REFRESH_TOKEN",   用户刷新access_token
	"openid":"OPENID",   				用户唯一标识
	"scope":"SCOPE" 					用户授权的作用域，使用逗号（,）分隔
	}

	{"errcode":40029,"errmsg":"invalid code"}	请求错误


	这个url有三种

	1、$url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=$appid&secret=$appsecret&code=$code&grant_type=authorization_code";



	2、$url = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=$appid&secret=$appsecret&code=$code&grant_type=authorization_code";



	3、$url = "https://api.weixin.qq.com/sns/jscode2session?appid=$appid&secret=$appsecret&js_code=$code&grant_type=authorization_code";



	 */
	public static String getWxInfoByCode(String code){
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Wechat.APP_ID+"&secret="+Wechat.APPS_SECRET
				+"&code="+code+"&grant_type=authorization_code";
		String backData=HttpsGet.doGet(url,null);
		if(backData.contains("errcode")){//请求错误
			return null;
		}
		return backData;
	}

	/**
	 * 通过参数获取微信用户信息
	 * @param infoJson {"scope":"xxx","access_token":"xxxx","openid":"xxxx"}
	 * @return
	 */
	public static String getWechatUserInfo(String infoJson){
		JSONObject jo = JSONObject.fromObject(infoJson);
		if("snsapi_userinfo".equals(jo.getString("scope"))){
			String userInfoJSON = HttpsGet.doGet("https://api.weixin.qq.com/sns/userinfo?access_token="+jo.getString("access_token")+"&openid="+jo.getString("openid")+"&lang=zh_CN",null);
			System.out.println("用户信息："+userInfoJSON);
			return userInfoJSON;
		}
		return null;
	}

	/**
	 * 获取acess_token
	 * 有效期7200秒,需缓存
	 * @return
	 * @throws Exception
	 */
	public static String getAccessToken(HttpServletRequest request) throws Exception{
		int differTime = 7200;
		HttpSession httpSession = null;
		if(request != null){
			httpSession = request.getSession();
			String oldDate = (String) httpSession.getAttribute(httpSession.getId()+"acess_token_time");
			if(oldDate != null){
				differTime =  Kit.getBetweenSecond(oldDate, Kit.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
		}

		String accessToken = "";
		if(differTime < 7200){
			accessToken = (String) httpSession.getAttribute(httpSession.getId()+"acess_token");
		}else{
			String url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+Wechat.APP_ID+"&secret="+Wechat.APPS_SECRET+"";
			String backData=HttpsGet.doGet(url,null);
			accessToken = JSONObject.fromObject(backData).get("access_token").toString();
			if(httpSession != null){
				httpSession.setAttribute(httpSession.getId()+"acess_token",accessToken);
				httpSession.setAttribute(httpSession.getId()+"acess_token_time",Kit.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
		}
		return accessToken;
	}

	/***
	 * 获取JSSDK权限验证配置信息
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getJSApiTicket(HttpServletRequest request,String pageUrl) throws Exception{
		//获取token
		String acess_token= getAccessToken(request);

		int differTime = 7200;
		HttpSession httpSession = null;
		if(request != null){
			httpSession = request.getSession();
			String oldDate = (String) httpSession.getAttribute(httpSession.getId()+"jsapi_ticket_time");
			if(oldDate != null){
				differTime =  Kit.getBetweenSecond(oldDate, Kit.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
		}

		String jsapi_ticket = "";
		if(differTime < 7200){
			jsapi_ticket = (String) httpSession.getAttribute(httpSession.getId()+"jsapi_ticket");//有效期7200秒,需缓存
		}else{
			String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+acess_token+"&type=jsapi";
			String backData=HttpsGet.doGet(urlStr,null);
			jsapi_ticket = (String) JSONObject.fromObject(backData).get("ticket");

			if(httpSession != null){
				httpSession.setAttribute(httpSession.getId()+"jsapi_ticket",jsapi_ticket);
				httpSession.setAttribute(httpSession.getId()+"jsapi_ticket_time",Kit.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
		}

		Map<String, String> ret = new HashMap<String, String>();

		String nonce_str = Kit.get32UUID();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String string1;
		String signature = "";

		//注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket +
				"&noncestr=" + nonce_str +
				"&timestamp=" + timestamp +
				"&url=" + pageUrl;
		System.out.println(string1);

		try{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		}catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}

		ret.put("appId", Wechat.APP_ID);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return  ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}


	/**
	 * 给微信发布模版消息
	 * @param wxMessage
	 * @return
	 */
	public static boolean sendTemplateMessage(String accessToken,WxMessage wxMessage){
		JSONObject obj = JSONObject.fromObject(wxMessage);
		System.out.println(obj.toString());
		try {
			String result = HttpsPost.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken,
					obj.toString(), "UTF-8");
			return result.contains("ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 发送充值通知[例子]
	 * @param openId	微信用户的openId
	 * @param account	充值账号
	 * @param amount	充值金额
	 * @return
	 */
	public static boolean sendRechargeInform(String accessToken,String openId,String account,String amount){
		WxMessage wm = new WxMessage();
		wm.setTouser(openId);
		wm.setTemplate_id("xQvfmVvz10I7eFfCwfQoAoFZ8Imgm90f0IRuow80dXM");
		Map<String,Property> data  = new HashMap<String, Property>();
		data.put("first", new Property("您好，您已成功进行融乐E家余额充值!"));
		data.put("accountType", new Property("充值账号"));
		data.put("account", new Property(account));
		data.put("amount", new Property(amount));
		data.put("result", new Property("充值成功"));
		data.put("remark", new Property("感谢使用融乐E家!"));
		wm.setData(data);

		return sendTemplateMessage(accessToken,wm);
	}


	/**
	 * @description 微信支付
	 * @param ip		ip地址
	 * @param batchNum		订单号
	 * @param body		描述
	 * @param money		金额
	 * @param notify_url		后台回调地址
	 * @param wechatPayType		微信支付类型（JSAPI：公众号支付，NATIVE：扫码支付，APP：app支付，MWEB：h5支付）
	 * @param returnUrl		前端回调地址
	 * @param openId		jsapi支付必传
	 * @date     2019年02月13日 10:13:26
	 * @author   cloud fly
	 */
	public static Object wechatPay(String ip, String batchNum, String body, String money, String notify_url, String wechatPayType,String returnUrl,String openId) {
		//money = "0.01";//测试默认值 TODO
		notify_url = Kit.PROJECT_PATH+"/" + notify_url;
		switch(wechatPayType){
			case "JSAPI":
				Map<String,String> resultMap = new HashMap<>();
				if(StringUtils.isEmpty(openId)) {
					resultMap.put("msg","openId必传");
					return resultMap;
				}
				return Wechat.wechatJsApiPay(ip,openId,batchNum,body,money,notify_url);
			case "NATIVE":
				return Wechat.wechatNATIVEPay(ip, batchNum, body, money, notify_url);
			case "APP":
				return  Wechat.wechatAppPay(ip,batchNum,body,money,notify_url);
			case "MWEB":
				return Wechat.wechatH5Pay(ip, batchNum, body, money, notify_url,returnUrl);
			default:
				return null;
		}
	}

	public static String getOpenidByCode(String code){
		try {

			if (StringUtils.isBlank(code)) {
				return null;
			}
			String backData = HttpsGet.doGet("https://api.weixin.qq.com/sns/oauth2/access_token?" +
					"appid=" + Wechat.APP_ID + "&secret=" + Wechat.APPS_SECRET + "&code=" + code + "&grant_type=authorization_code", null);
			if (backData.contains("errcode")) {//请求错误
				return null;
			}

			JSONObject json = JSONObject.fromObject(backData);
			String openid = json.getString("openid");

			return openid;

		}catch (Exception e){
			log.error("获取openid失败");
			return null;
		}

	}


}
