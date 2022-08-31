package com.qyh.demo.base.third.sms;

import com.qyh.demo.base.util.HttpsPost;
import com.qyh.demo.base.util.Kit;
import net.sf.json.JSONObject;


/**
 * 华信短信
 * @author qiuyuehao
 *
 */
public class HuaXinSms {
	
	private static final String SIGNATURE = "项目名";
	
	private static final String ACCOUNT = "8E****4";
	private static final String PASSWORD = "8E****487";
	

	public static void main(String[] args) throws Exception {
		System.out.println("asdlkfjalkdsfj");
		System.out.println(sendSMS("18113048397", "55555","code"));
		sendSMS("18113048397", "123123","code");
	}
	
	/**
	 * 发送短信
	 * @return 
	 * @throws Exception
	 */
	public static String sendSMS(String phones,String content,String type)throws Exception {
		String url = "https://dx.ipyy.net/smsJson.aspx";
		String param = "";
		if("code".equals(type)) {
			param = "action=send&userid=&account="+ACCOUNT+"&password="
					+ Kit.getMD5Str(PASSWORD)+"&mobile="+phones+"&content=【"+SIGNATURE+"】亲爱的用户，您的短信验证码为"+content+",10分钟内有效，若非本人操作请忽略。&sendTime=&extno=";
		}else{
			param = "action=send&userid=&account="+ACCOUNT+"&password="
					+ Kit.getMD5Str(PASSWORD)+"&mobile="+phones+"&content=【"+SIGNATURE+"】亲爱的用户："+content+"&sendTime=&extno=";
		}
		
		JSONObject js = JSONObject.fromObject(HttpsPost.post(url, param,"UTF-8"));
		return js.getString("returnstatus");
	}
	
}
