package com.qyh.demo.base.exception;

public class EasemobMsgLogException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971985159050386791L;
	
	
	public EasemobMsgLogException(String frdMessage)	{
		super(createFriendlyErrMsg(frdMessage));
	}
	
	public EasemobMsgLogException(String frdMessage,String mes)	{
		super(frdMessage);
	}

	public EasemobMsgLogException(Throwable throwable){
		super(throwable);
	}

	public EasemobMsgLogException(String frdMessage,Throwable throwable){
		super(createFriendlyErrMsg(frdMessage),throwable);
	}

	private static String createFriendlyErrMsg(String msgBody) {
		String prefixStr = "抱歉，";
		String suffixStr = " 请稍后再试或与管理员联系！";
		StringBuffer friendlyErrMsg = new StringBuffer("");
		friendlyErrMsg.append(prefixStr);
		friendlyErrMsg.append(msgBody);
		friendlyErrMsg.append(suffixStr);
		return friendlyErrMsg.toString();
	}

}
