package com.qyh.demo.base.third.Jdpush;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class Jdpush {
	/**
	 * 律宝宝律师端
	 */
	private static final String APP_KEY_SHOP = "64585a4223670f4852b56235";
	private static final String MASTER_SHOP = "f2cf0190750f553908771cc5 ";
	
	/**
	 * 律宝宝用户端
	 */
	private static final String APP_KEY_MEMBER = "db94a825309fd80d77898139";
	private static final String MASTER_MEMBER = "5f398785e1e3861992fddeb4";
	
	/**
	 * 测试账号
	 */
//	private static final String APP_KEY = "003c0d88eaa1f5c466bd0***";
//	private static final String MASTER = "d68c56f7d8dc376bfa229***";
	
	//IOS开发环境	发布环境：true/测试环境：false
	private static final boolean VERSIONS = true;
	
	/**
	 * 推送消息给指定别名
	 * @param msg		推送信息
	 * @param hideMsg	隐藏信息
	 * @param alias		发送对象ID数组
	 * @param sound		IOS提示语音	默认用：default
	 */
	public static void sendPushAliasShop(String msg,String hideMsg,String[] alias,String sound){
		JPushClient jpush = new JPushClient(MASTER_SHOP, APP_KEY_SHOP);
		PushPayload pp = PushPayload.newBuilder()
                .setPlatform(Platform.all())//必填    推送平台设置  
                .setAudience(Audience.alias(alias))  //别名
                .setNotification(Notification.newBuilder()
                		.setAlert(msg)
                		.addPlatformNotification(IosNotification.newBuilder()
                				.incrBadge(1)
                				.setSound(sound)
                				.addExtra("msg", hideMsg)
                				.build()
                				)
                		.build())
                 .setOptions(Options.newBuilder()  
                         .setApnsProduction(VERSIONS)//IOS:测试环境用false,发布环境用true
                         .build())
                         .setMessage(Message.content(hideMsg))
                .build();  
		
		try {
			 PushResult result = jpush.sendPush(pp);
			 System.out.println(result.isResultOK());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 推送消息给指定标签
	 * @param msg		推送信息
	 * @param hideMsg	隐藏信息
	 * @param tags		发送标签数组
	 * @param sound		IOS提示语音	默认用：default
	 */
	public static void sendPushTagShop(String msg,String hideMsg,String[] tags,String sound){
		JPushClient jpush = new JPushClient(MASTER_SHOP, APP_KEY_SHOP);
		PushPayload pp = PushPayload.newBuilder()  
                .setPlatform(Platform.all())//必填    推送平台设置  
                .setAudience(Audience.tag(tags)) //标签
                .setNotification(Notification.newBuilder()
                		.setAlert(msg)
                		.addPlatformNotification(IosNotification.newBuilder()
                				.incrBadge(1)
                				.setSound(sound)
                				.addExtra("msg", hideMsg)
                				.build())
                		.build())
        		 .setOptions(Options.newBuilder()  
                 .setApnsProduction(VERSIONS)//IOS:测试环境用false,发布环境用true
                 .build())  
                 .setMessage(Message.content(hideMsg))
                .build();  
		
		try {
			jpush.sendPush(pp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
}  