package com.qyh.demo.base.third.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝
 * 
 * @author qiuyuehao
 *
 */
@Slf4j
public class Alipay {
	/**
	 * 应用的APPID
	 */
	public static final String APP_ID = "20180****0706437";
	/**
	 * 应用的PID
	 */
	public static final String PID = "208813****566301";
	/**
	 * 商户私钥
	 */
	public static final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDL****9HNaNxroQIgdJtzyiz1agucAg2k6uUp8xUC14OWrsl7yu2lW1qBKSyRc2w7dQbT4l/zbu8wX6K9lewtCtBc7kNsa4dd+AdMc8O/Kahz9BpSFLjG+DbPYii40VGfqB+cJc9rx2B02haqLi3kUuMF5GmolZ3/n58DPWBAxl5XIdlB9bPj3CvyhkB00X9q7hjxrSwV8eorQVAW/s8kJ7eP3zSmP2i+8mA3YQ+Ms/Q072TB1ZK3+hk0LOIrDIgSnIcq5Ca8XF1pDq2O98sKCzlv1Mvb1b0yq/xJDchx8y5REXXPb+TYTx/GrC/ZgoNeFqbXwwnyX68pRdOtRIJqrAgMBAAECggEBAJOLhU/O7F0E2s8M0dQbfzXSYGd34EdHLxIxuCKbSD7XeQAZO1ApRISItThDDc9Gcq3YJZbREiatbb2QkpmtoKh8GsRynjgRB3HJ8Yp8nDT2kMheF1YIEKp3WcZb2uS4zCP+SZ9cQhidXdtVjmaAkxyyehl3TtSNMyVL/Yyh9GGQ6OGYMpYzBgY4k/4b3jZ+jdPUnOzy5mbX1gJ+zp96b67Wr0WbJ/0gQsDxRVP7l1oWwN9Wp6jzIjSy+GmU+SaMb9VEATBRiR0/sNzvFKpD/PlNaKoL6sOcXKjWTvPhej4xT6EbPMTvfY9q89nmaYpM21hDO7NmV/53tm6PjrjhJ4ECgYEA7rQe4kBCSkAmcD1KiT7VJgZqT9pbMoqw8CVi0jpMDKinvTt7vTqahXdSPP6nTqQKT+xDK34ugsjmqXa+qsm1+mKTvetWaD1785EkqRaDpGXUMVqesTzf3OmohPCRea343npo2ENG7h98CRzAaqNqzGPnQAb9zcl6j8KBXE3XYEkCgYEA2dPEB0429oE2vm28it6hcGzTZEbcXKK63a1eSfFU5gO3/K8ZNiv+PERkBhP7arvkRP/2V4/0E4Ho3wGgcoSDG0AwfoUSQLR6aqFRw/BNkVR1JO2qUJBi3lOVUQAx0iwnKobVAEHzAnxyFz9NSoUf0Nul4BbBDRNx2QC+qUBfS1MCgYBpx2QQOw2lPbxK7L5FB5UJHrqp3EehpBg4sziRlMfyrCJ5Fw0ElB25m2DUKr0VelgfhsFVWrtdtY2R908S/AQqitiRxuIpm2V8UAuA6rs3dPCNVm0QebtyeBD3qGuvEktAu9oOS/H4v/cFSrpyByzimjr2AqMiOHCZmEYfE1eoEQKBgQDJAX1XMHnPpxUZTLI7g4197S50zl7yrMe7YAwuN6CcULorVnjxYE9O1d1nLnFG4POYKIrt6kzU44A8qYehpV5Ge0lEtw9M3j5Iv2xwXwN1JHalCRutFE/n0UFsab2PsGbD+3/6qODSXnv5r9Rm+8mMNeQvkU+7/UGFegZ2sIw/OQKBgQDRTOUBOg8rvqLGdCia7s70zbs/8X0v/ngPHaw4cG+lkZ9K9mQEv99XtNFPUhuSYWlm6mE4aVeR5pt7uKCKNtKmv2D2IgJ8cu3dSpyB5J0lhIBJK6/ZqvxbYZz3+jzLqXznUgzn6jBNzosjXggPEyRoD3+vyxqxpMgmiRSjbOzvBA==";
	/**
	 * 支付公钥
	 */
	public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmkfPmir****SzM+zyNnqYaxOho45aa7x5humxrlf6R0sWPbTQxZJhs0yTYuhb6+cMb/hd64ibeaGTmlIaZZQOX0u6AVCbwNcn+dR+k2Jznsd3reNr2AYezBA4CgxzeGPSI8dZcXArhuO8qR5Ma36A1xhwKFtMuFYgxpnx7FkEd1KGwIbrBabIM3mK9oAdMzbA54InG7DADGOEj9jVF80C2conDgjG1px4AFFHCU87/qlDZ1rfh27Ic4rrHveCQyDNH7eP8S3bakHlE0/NgCsw0K2u5kDyn1PUolEuAwceN2oiFCtX4eKJ3Ecbrz32eOtYOo8+u5nY+Vuhn6uqh3T7wIDAQAB";





	/**
	 * 编码格式
	 */
	public static final String CHARSET = "utf-8";
	/**
	 * 接口名称:APP支付
	 */
	public static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

	/**
	 * 退款接口名称：统一收单交易退款接口
	 */
	public static final String REFUND = "alipay.trade.refund";


	/**
	 * 支付宝登录   授权认证接口
	 */
	public static final String AUTH = "com.alipay.account.auth";
	/**
	 * 获取授权令牌接口
	 */
	public static final String TOKEN = "alipay.system.oauth.token";
	/**
	 * 用户信息共享接口
	 */
	public static final String SHARE = "alipay.user.info.share";

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	public static final String ALIPAY_GATEWAY_NEW = "https://openapi.alipay.com/gateway.do";
	/**
	 * 加密方式
	 */
	public static final String SIGN_TYPE = "RSA2";

	/**
	 * 数据类型
	 */
	public static final String FORMAT = "json";

	/**
	 * 公共  请求对象
	 */
	public static final AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);



	/**
	 * app支付
	 *
	 * @author qiuyuehao 2017年10月27日下午7:18:29
	 * @param number
	 *            订单号
	 * @param describe
	 *            支付时的描述
	 * @param price
	 *            总金额
	 * @param notify_url
	 *            异步通知地址
	 * @return
	 */
	public static String appPay(String number, String describe, String price, String notify_url) {
		// 实例化客户端
		AlipayClient alipayClient = null;

		alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET,
				ALIPAY_PUBLIC_KEY, SIGN_TYPE);

		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("可以传点别的");
		model.setSubject(describe);
		model.setOutTradeNo(number);
		model.setTimeoutExpress("24h");// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
		// 注：若为空，则默认为15d。
		model.setTotalAmount(price);
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(notify_url);
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			log.info("支付参数信息==="+response.getBody());
			log.info("code==="+response.getBody());
			log.info("msg==="+response.getMsg());
			return response.getBody();// 就是orderString 可以直接给客户端请求，无需再做处理。
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return "";
	}



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
	 * @author qiuyuehao
	 */
	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model) throws AlipayApiException{
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return alipayClient.execute(request);
	}

	/**
	 * 获取授权信息
	 * @param code
	 * @return
	 * 2018年9月10日
	 * @author qiuyuehao
	 */
	public static AlipaySystemOauthTokenResponse token(String code) {
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();// 创建API对应的request类
		request.setGrantType("authorization_code");
		request.setCode(code);
		AlipaySystemOauthTokenResponse response = null;
		AlipayClient alipayClient = null;
		alipayClient = new DefaultAlipayClient(ALIPAY_GATEWAY_NEW, APP_ID, APP_PRIVATE_KEY, "json",
				CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

		try {
			response = alipayClient.execute(request);

		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 获取用户信息共享
	 * @param accessToken
	 * @return
	 * 2018年9月10日
	 * @author qiuyuehao
	 */
	public static AlipayUserInfoShareResponse share(String accessToken) {
		AlipayClient alipayClient = null;
		alipayClient = new DefaultAlipayClient(ALIPAY_GATEWAY_NEW, APP_ID, APP_PRIVATE_KEY, "json",
				CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
		AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
		AlipayUserInfoShareResponse response = null;
		try {
			response = alipayClient.execute(request, accessToken);
			if (response.isSuccess()) {
				System.out.println("调用成功");
				System.out.println(response.getBody());
			} else {
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @description 网站支付2.0
	 * @param number
	 *            订单号
	 * @param describe
	 *            支付时的描述
	 * @param price
	 *            总金额
	 * @param notify_url
	 *            异步通知地址
	 * @param returnUrl
	 *            前端回调地址
	 * @date     2019年01月26日 15:10:50
	 * @author qiuyuehao
	 */
	public static String wapPay(String number, String describe, String price, String notify_url,String returnUrl) {
		// 实例化客户端
		AlipayClient alipayClient = null;

		alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET,
				ALIPAY_PUBLIC_KEY, SIGN_TYPE);

		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.wap.pay(手机网站支付接口2.0)
		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		//model.setQuitUrl(Kit.PROJECT_PATH+"/");//用户付款中途退出返回商户网站的地址
		model.setBody("可以传点别的");
		model.setSubject(describe);
		model.setOutTradeNo(number);
		model.setTimeoutExpress("24h");// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
		// 注：若为空，则默认为15d。
		model.setTotalAmount(price);
		model.setProductCode("QUICK_WAP_WAY");
		request.setBizModel(model);
		request.setNotifyUrl(notify_url);
		request.setReturnUrl(returnUrl);//web 端一部回调地址"https://www.baidu.com"
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			//AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			AlipayTradeWapPayResponse response = alipayClient.sdkExecute(request);
			log.info("支付参数信息==="+response.getBody());
			log.info("code==="+response.getBody());
			log.info("msg==="+response.getMsg());
			return response.getBody();// 就是orderString 可以直接给客户端请求，无需再做处理。
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return "";
	}


	/**
	 * @description PC场景下单并支付(统一收单下单并支付页面接口)
	 * @param number
	 *            订单号
	 * @param describe
	 *            支付时的描述
	 * @param price
	 *            总金额
	 * @param notify_url
	 *            异步通知地址
	 * @param returnUrl
	 *            前端回调地址
	 * @date     2019年01月26日 15:25:20
	 * @author qiuyuehao
	 */
	public static String pagePay(String number, String describe, String price, String notify_url,String returnUrl) {
		// 实例化客户端
		AlipayClient alipayClient = null;

		alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET,
				ALIPAY_PUBLIC_KEY, SIGN_TYPE);

		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.page.pay(统一收单下单并支付页面接口)
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradePagePayModel model = new AlipayTradePagePayModel();
		//model.setQuitUrl(Kit.PROJECT_PATH+"/");//用户付款中途退出返回商户网站的地址
		model.setBody("可以传点别的");
		model.setSubject(describe);
		model.setOutTradeNo(number);
		model.setTimeoutExpress("24h");// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
		// 注：若为空，则默认为15d。
		model.setTotalAmount(price);
		model.setProductCode("FAST_INSTANT_TRADE_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(notify_url);
		request.setReturnUrl(returnUrl);
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradePagePayResponse response = alipayClient.sdkExecute(request);
			log.info("支付参数信息==="+response.getBody());
			log.info("code==="+response.getBody());
			log.info("msg==="+response.getMsg());
			return response.getBody();// 就是orderString 可以直接给客户端请求，无需再做处理。
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return "";
	}



}
