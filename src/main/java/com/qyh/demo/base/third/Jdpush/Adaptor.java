package com.qyh.demo.base.third.Jdpush;


/**
 * 适配器类
 * @author qiuyuehao
 *
 */
public class Adaptor {
	
	/**
	 * 推送消息给指定别名
	 * @param msg	推送信息
	 * @param alias	发送对象ID数组
	 * @throws Exception
	 */
	public static void sendPushAliasShop(String msg,String[] alias){
		Jdpush.sendPushAliasShop(msg, "", alias, "default");
	}

	/**
	 * 推送消息给指定别名[有隐藏信息]
	 * @param msg		显示信息
	 * @param hideMsg	隐藏信息
	 * @param alias		别名数组
	 */
	public static void sendPushAliasShop(String msg,String hideMsg,String[] alias){
		Jdpush.sendPushAliasShop(msg, hideMsg, alias, "default");
	}
	
	/**
	 * 推送消息给指定别名[有隐藏信息及设置语音]
	 * @param msg		显示信息
	 * @param hideMsg	隐藏信息
	 * @param alias		别名数组
	 * @param sound		语音
	 */
	public static void sendPushAliasShop(String msg,String hideMsg,String[] alias,String sound){
		Jdpush.sendPushAliasShop(msg, hideMsg, alias, sound);
	}

	
	/**
	 * 推送消息给指定标签[有隐藏信息及设置语音]
	 * @param msg		显示信息
	 * @param hideMsg	隐藏信息
	 * @param tags		标签数组
	 * @param sound		语音
	 */
	public static void sendPushTagShop(String msg,String hideMsg,String[] tags,String sound){
		Jdpush.sendPushTagShop(msg, hideMsg, tags, sound);
	}

}
