package com.qyh.demo.base.exception;

/**
 * 短信发送异常
 * @author yh
 *
 */
public class SMSException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971985159050386791L;
	
	public SMSException(String frdMessage)	{
		super(createFriendlyErrMsg(frdMessage));
	}
	
	public SMSException(String frdMessage,String mes)	{
		super(frdMessage);
	}

	public SMSException(Throwable throwable){
		super(throwable);
	}

	public SMSException(String frdMessage,Throwable throwable){
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
