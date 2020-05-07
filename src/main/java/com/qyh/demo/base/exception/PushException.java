package com.qyh.demo.base.exception;

/**
 * 系统参数异常
 * @author yh
 *
 */
public class PushException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971985159050386791L;
	
	public PushException(String frdMessage)	{
		super(createFriendlyErrMsg(frdMessage));
	}
	
	public PushException(String frdMessage,String mes)	{
		super(frdMessage);
	}

	public PushException(Throwable throwable){
		super(throwable);
	}

	public PushException(String frdMessage,Throwable throwable){
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
