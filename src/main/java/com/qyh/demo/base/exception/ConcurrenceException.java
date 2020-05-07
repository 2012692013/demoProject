package com.qyh.demo.base.exception;

/**
 * 并发异常
 * @author yh
 *
 */
public class ConcurrenceException extends RuntimeException{

	private static final long serialVersionUID = 2971985159050386791L;
	
	
	public ConcurrenceException(String frdMessage)	{
		super(createFriendlyErrMsg(frdMessage));
	}
	
	public ConcurrenceException(String frdMessage, String mes)	{
		super(frdMessage);
	}

	public ConcurrenceException(Throwable throwable){
		super(throwable);
	}

	public ConcurrenceException(String frdMessage, Throwable throwable){
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
