package com.qyh.demo.base.third.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.qyh.demo.base.third.pay.alipay.Alipay;
import com.qyh.demo.base.util.Kit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class AlipayUtil {

	/**
	 * @description 支付宝支付
	 * @param number		订单号
	 * @param describe		支付时的描述
	 * @param price			总金额
	 * @param notify_url	异步通知地址
	 * @param payType	支付类型(app:app支付，wap：网站支付2.0，page：PC场景下单并支付)
	 * @param returnUrl	前端回调地址
	 * @date     2019年02月19日 16:05:14
	 * @author   cloud fly
	 */
	public static String alipayPay(String number,String describe,String price,String notify_url,String payType,String returnUrl) throws Exception{
		//price = "0.01";//测试默认值 TODO
		notify_url = Kit.PROJECT_PATH+"/"+notify_url;
		switch(payType){
			case "app":
				return Alipay.appPay(number, describe, price, notify_url);
			case "wap":
				return Alipay.wapPay(number, describe, price, notify_url,returnUrl);
			case "page":
				return Alipay.pagePay(number, describe, price, notify_url,returnUrl);
			default:
				return "";

		}
	}
	/**
	 * 支付宝退款
	 * @author lcj 2017年10月27日下午9:10:35
	 * @param paramList	退款参数
	 * @return	AlipayTradeRefundResponse对象参数说明：https://docs.open.alipay.com/api_1/alipay.trade.refund
	 * @throws Exception
	 */
/*	public static AlipayTradeRefundResponse alipayRefund(AlipayRefund paramList) throws Exception{
		return Alipay.refund(paramList);
	}*/

	/**
	 * 支付宝退款
	 * @param model 必传属性(outRequestNo:退款批次号,outTradeNo:支付订单号,outTradeNo:退款金额,
	 * refundReason:退款原因,tradeNo:支付宝交易号)
	 * @return  AlipayTradeRefundResponse{code:10000(接口调用成功,结课参考业务返回值),code:20000(服务不可用,网关本身的问题),详情参考：https://docs.open.alipay.com/common/105806
	 * msg:网关返回码描述
	 * sub_code:ACQ.INVALID_PARAMETER(参数无效)...,sub_msg:业务返回描述
	 * }
	 * @throws AlipayApiException
	 * @describe 详情参考地址 https://docs.open.alipay.com/api_1/alipay.trade.refund
	 * 2018年11月7日
	 * @author yh
	 */
	public static AlipayTradeRefundResponse alipayRefund(AlipayTradeRefundModel model) throws Exception{
		return Alipay.tradeRefundToResponse(model);
	}

	/**
	 * 得到支付宝回调传来的参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getAlipayParams(HttpServletRequest request){
		Map requestParams = request.getParameterMap();

		Map<String,String> params = new HashMap<String,String>();

		StringBuffer paramsStr = new StringBuffer("");
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);

			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			paramsStr.append(name+"="+valueStr+"&");
			params.put(name, valueStr);
		}

		System.out.println("支付宝回调函数："+paramsStr.toString());
		return params;
	}

	/**
	 * 验证是否是支付宝发出的请求
	 * @param params
	 * @return
	 */
	public static boolean alipayVerify(Map<String,String> params){
		try {
			return AlipaySignature.rsaCheckV1(params, Alipay.ALIPAY_PUBLIC_KEY, Alipay.CHARSET, Alipay.SIGN_TYPE);
		} catch (AlipayApiException e) {
			return false;
		}
	}

	/**
	 * 给支付宝回调返回状态
	 * @param response
	 * @param isOk
	 * @throws Exception
	 */
	public static void returnState(HttpServletResponse response,boolean isOk) throws Exception{
		if(isOk){
			response.getWriter().write("success");
		}else{
			response.getWriter().write("fail");
		}
	}
	/**
	 * 获取授权信息
	 * @return
	 * @throws Exception
	 * @author shugl
	 * @date 2018年7月25日
	 */
	public static String getAuthInfo(String type) throws Exception{
		String content = "";
		String sign = "";
		if("user".equals(type)) {//用户端
			content="apiname="+Alipay.AUTH+"&app_id="+Alipay.APP_ID+
					"&app_name=mc&auth_type=AUTHACCOUNT&biz_type=openservice&method=alipay.open.auth.sdk.code.get"
					+ "&pid="+Alipay.PID+"&product_id=APP_FAST_LOGIN&scope=kuaijie&target_id="+System.currentTimeMillis()+"&sign_type=RSA2";
			sign=AlipaySignature.rsaSign(content, Alipay.APP_PRIVATE_KEY, Alipay.CHARSET, Alipay.SIGN_TYPE);
		}else{//律师端
			content="apiname="+Alipay.AUTH+"&app_id="+Alipay.APP_ID+
					"&app_name=mc&auth_type=AUTHACCOUNT&biz_type=openservice&method=alipay.open.auth.sdk.code.get"
					+ "&pid="+Alipay.PID+"&product_id=APP_FAST_LOGIN&scope=kuaijie&target_id="+System.currentTimeMillis()+"&sign_type=RSA2";
			sign=AlipaySignature.rsaSign(content, Alipay.APP_PRIVATE_KEY, Alipay.CHARSET, Alipay.SIGN_TYPE);
		}
		String enCodesign = URLEncoder.encode(sign, "UTF-8");
		String authInfo = content+"&sign="+enCodesign;
		return authInfo;
	}

}
