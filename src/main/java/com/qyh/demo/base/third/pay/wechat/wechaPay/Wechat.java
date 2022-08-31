package com.qyh.demo.base.third.pay.wechat.wechaPay;

import com.qyh.demo.base.util.Kit;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * 微信
 * @author qiuyuehao
 *
 */
public class Wechat {
	/**开放平台:微信分配的公众账号ID*/
	public static final String APP_ID = "wx9238****111e9a17";
	/**开放平台:微信分配的应用密钥*/
	public static final String APPS_SECRET="0389a84121****634226bec3aac79223";
	/**微信支付分配的商户号*/
	public static final String MCH_ID = "151****711";
	/**设置的KEY值*/
	public static final String API_KEY = "d4e2c3b5d5****6200fb7dd132aba77b";



	/**公众号平台:微信分配的公众账号ID*/
	public static final String JS_APP_ID = "wx7fda****328d804c";
	/**公众号平台:微信分配的应用密钥*/
	public static final String JS_APPS_SECRET="4498d1bfd2****7a75375fc8798d0942";
	/**微信支付分配的商户号*/
	public static final String JS_MCH_ID = "148****452";
	/**设置的KEY值*/
	public static final String JS_API_KEY = "8fd632b2f****b578146c66858cfca3e";


	/**微信统一下单地址*/
	public static final String UNIFY_PLACE_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**微信退款地址*/
	public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	/**
	 * APP支付接口接口
	 * @param ip  ip地址
	 * @param number  订单号
	 * @param body  描述
	 * @param money  金额
	 * @param notify_url  后台回调地址
	 * @return
	 */
	public static Map<String, String> wechatAppPay(String ip,String number,String body,String money,String notify_url){
		BigDecimal b1 = new BigDecimal(money);
		BigDecimal b2 = new BigDecimal("100");
		money = b1.multiply(b2).intValue()+"";

		Map<String, String> parame = new HashMap<String, String>();

		parame.put("appid", APP_ID);
		parame.put("mch_id", MCH_ID);

		parame.put("nonce_str", Kit.get32UUID());
		parame.put("body", body);
		parame.put("out_trade_no", number);
		parame.put("total_fee", money);
		parame.put("spbill_create_ip", ip);
		parame.put("notify_url", notify_url);
		parame.put("trade_type", "APP");//JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付

		/**
		 * 设置支付时间
		 */
		/* String startDateTime = Kit.formatDateTime(new Date(), "yyyyMMddHHmmss");
		 String endDateTime = Kit.formatDateTime(new Date(new Date().getTime()+60000), "yyyyMMddHHmmss");//1分钟过期时间
		 parame.put("time_start", startDateTime);//交易开始时间
		 parame.put("time_expire", endDateTime);//交易结束时间*/

		String sign = "";
		sign = AssistUtil.buildRequestMysign(parame,API_KEY);
		parame.put("sign", sign);

		//第一步：从微信的服务器获取prepayId
		Map<String, String> map = AssistUtil.httpsRequestToXML(UNIFY_PLACE_URL, "POST", AssistUtil.conversionMapToXml(parame));
		String prepay_Id = map.get("prepay_id");

		//第二步：根据prepayId生成签名参数
		Map<String,String> paramMap = new HashMap<String, String>();

		if(StringUtils.isNotEmpty(prepay_Id)){
			paramMap.put("prepayid", prepay_Id);
			paramMap.put("noncestr", Kit.get32UUID());
			paramMap.put("package", "Sign=WXPay");
			paramMap.put("timestamp", (System.currentTimeMillis() / 1000)+"");
			paramMap.put("appid", APP_ID);
			paramMap.put("partnerid", MCH_ID);
			String sig = AssistUtil.getSignCommon("UTF-8", paramMap, API_KEY);
			paramMap.put("sign", sig);


		}

		return paramMap;
	}

	/**
	 * 微信支付[公众号支付]
	 * @param ip     ip地址
	 * @param number  订单号
	 * @param body   描述
	 * @param money   金额
	 * @param notify_url  后台回调地址
	 * @return
	 */
	public static Map<String, String> wechatJsApiPay(String ip,String openid,String number,String body,String money,String notify_url){
		BigDecimal b1 = new BigDecimal(money);
		BigDecimal b2 = new BigDecimal("100");
		money = b1.multiply(b2).intValue()+"";

		Map<String, String> parame = new HashMap<String, String>();
		parame.put("appid", JS_APP_ID);
		parame.put("mch_id", JS_MCH_ID);
		parame.put("openid", openid);
		parame.put("nonce_str", Kit.get32UUID());
		parame.put("body", body);
		parame.put("out_trade_no", number);
		parame.put("total_fee", money);
		parame.put("spbill_create_ip", ip);
		parame.put("notify_url", notify_url);
		parame.put("trade_type", "JSAPI");//JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付

		String sign = AssistUtil.buildRequestMysign(parame,JS_API_KEY);
		parame.put("sign", sign);

		//第一步：从微信的服务器获取prepayId
		Map<String, String> map = AssistUtil.httpsRequestToXML(UNIFY_PLACE_URL, "POST", AssistUtil.conversionMapToXml(parame));
		String prepay_Id = map.get("prepay_id");

		//第二步：根据prepayId生成签名参数
		Map<String,String> paramMap = new HashMap<String, String>();
		if(StringUtils.isNotEmpty(prepay_Id)){
			paramMap.put("appId", JS_APP_ID);
			paramMap.put("nonceStr", Kit.get32UUID());
			paramMap.put("package", "prepay_id="+prepay_Id);
			paramMap.put("signType", "MD5");
			paramMap.put("timeStamp", (System.currentTimeMillis() / 1000)+"");
			String sig = AssistUtil.getSignCommon("UTF-8", paramMap, JS_API_KEY);
			paramMap.put("paySign", sig);
		}

		return paramMap;
	}


	/**
	 * 微信退款（要安装证书）
	 * @param path		证书地址
	 * @throws Exception
	 */
	public static Map<String, String> refund(String appId,String mchId,String apiKey,String transaction_id,String total_fee,String refund_fee,String batch_no,String path) throws Exception{
		BigDecimal refund = new BigDecimal(refund_fee);
		BigDecimal cl = new BigDecimal("100");
		refund_fee = refund.multiply(cl).intValue()+"";

		BigDecimal total = new BigDecimal(total_fee);
		total_fee = total.multiply(cl).intValue()+"";

		Map<String, String> parame = new HashMap<String, String>();
		parame.put("appid", appId);
		parame.put("mch_id", mchId);
		parame.put("op_user_id", mchId);
		parame.put("nonce_str", Kit.get32UUID());
		parame.put("out_refund_no", batch_no);
		parame.put("refund_fee", refund_fee);
		parame.put("total_fee", total_fee);
		parame.put("transaction_id", transaction_id);

		String sign = AssistUtil.buildRequestMysign(parame,apiKey);
		parame.put("sign", sign);

		//发起退款
		String xmlStr = AssistUtil.conversionMapToXml(parame);
		String refundStr = AssistUtil.doRefund(REFUND_URL, xmlStr, mchId,path);
		Map<String, String> map = AssistUtil.xmlToMap(refundStr);

		return map;
	}


	/**
	 * @description 微信扫码支付
	 * @param ip		ip地址
	 * @param batchNum		订单号
	 * @param body		描述
	 * @param money		金额
	 * @param notify_url		后台回调地址
	 * @date     2019年02月13日 15:30:12
	 * @author qiuyuehao
	 */
	public static Object wechatNATIVEPay(String ip, String batchNum, String body, String money, String notify_url) {
		BigDecimal b1 = new BigDecimal(money);
		BigDecimal b2 = new BigDecimal("100");
		money = b1.multiply(b2).intValue()+"";
		Map<String, String> parame = new HashMap<>();
		parame.put("appid", APP_ID);
		parame.put("mch_id", MCH_ID);
		parame.put("nonce_str", Kit.get32UUID());
		parame.put("body", body);
		parame.put("out_trade_no", batchNum);
		parame.put("total_fee", money);
		parame.put("spbill_create_ip", ip);
		parame.put("notify_url", notify_url);
		parame.put("product_id",batchNum);//商品id
		parame.put("trade_type", "NATIVE");//JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付,H5支付的交易类型为MWEB
		String sign = AssistUtil.buildRequestMysign(parame,API_KEY);
		parame.put("sign", sign);


		//第一步：从微信的服务器获取prepayId
		Map<String, String> map = AssistUtil.httpsRequestToXML(UNIFY_PLACE_URL, "POST", AssistUtil.conversionMapToXml(parame));
		String prepay_Id = map.get("prepay_id");

		//第二步：根据prepayId生成签名参数
		Map<String,String> paramMap = new HashMap<>();
		if(StringUtils.isNotEmpty(prepay_Id)){
			paramMap.put("appId", APP_ID);
			paramMap.put("mch_id", MCH_ID);
			paramMap.put("prepay_Id",prepay_Id );
			paramMap.put("trade_type",map.get("trade_type") );
			paramMap.put("code_url",map.get("code_url") );
			String sig = AssistUtil.getSignCommon("UTF-8", paramMap, API_KEY);
			paramMap.put("sign", sig);
		}

		return paramMap;
	}

	/**
	 * @description 微信h5支付
	 * @param ip		ip地址
	 * @param batchNum		订单号
	 * @param body		描述
	 * @param money		金额
	 * @param notify_url		后台回调地址
	 * @param returnUrl		前端回调地址
	 * @date     2019年02月13日 14:27:34
	 * @author qiuyuehao
	 */
	public static Object wechatH5Pay(String ip, String batchNum, String body, String money, String notify_url,String returnUrl) {
		BigDecimal b1 = new BigDecimal(money);
		BigDecimal b2 = new BigDecimal("100");
		money = b1.multiply(b2).intValue()+"";
		Map<String, String> parame = new HashMap<String, String>();
		parame.put("appid", APP_ID);
		parame.put("mch_id", MCH_ID);

		parame.put("nonce_str", Kit.get32UUID());
		parame.put("body", body);
		parame.put("out_trade_no", batchNum);
		parame.put("total_fee", money);
		parame.put("spbill_create_ip", ip);
		parame.put("notify_url", notify_url);
		JSONObject sceneInfoJson = new JSONObject();
		JSONObject j = new JSONObject();
		j.put("type","Wap");
		j.put("wap_url",returnUrl);
		j.put("wap_name","手机充值");
		sceneInfoJson.put("h5_info",returnUrl);
		parame.put("scene_info",j.toString());
		parame.put("trade_type", "MWEB");//JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付,H5支付的交易类型为MWEB

		String sign = AssistUtil.buildRequestMysign(parame,API_KEY);
		parame.put("sign", sign);

		//第一步：从微信的服务器获取prepayId
		Map<String, String> map = AssistUtil.httpsRequestToXML(UNIFY_PLACE_URL, "POST", AssistUtil.conversionMapToXml(parame));
		String prepay_Id = map.get("prepay_id");

		//第二步：根据prepayId生成签名参数
		Map<String,String> paramMap = new HashMap<>();
		if(StringUtils.isNotEmpty(prepay_Id)){
			paramMap.put("appId", APP_ID);
			paramMap.put("mch_id", MCH_ID);
			paramMap.put("nonceStr", Kit.get32UUID());
			paramMap.put("prepay_Id",prepay_Id );
			paramMap.put("trade_type",map.get("trade_type") );
			paramMap.put("mweb_url",map.get("mweb_url") );
			paramMap.put("signType", "MD5");
			paramMap.put("timeStamp", (System.currentTimeMillis() / 1000)+"");
			String sig = AssistUtil.getSignCommon("UTF-8", paramMap, API_KEY);
			paramMap.put("sign", sig);
		}

		return paramMap;
	}

}
