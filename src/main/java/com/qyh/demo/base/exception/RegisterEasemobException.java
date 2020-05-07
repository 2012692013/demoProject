package com.qyh.demo.base.exception;

/**
 * 系统参数异常
 * @author yh
 *
 */
public class RegisterEasemobException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971985159050386791L;
	
	public RegisterEasemobException(String frdMessage)	{
		super(createFriendlyErrMsg(frdMessage));
	}
	
	public RegisterEasemobException(String frdMessage,String mes)	{
		super(frdMessage);
	}

	public RegisterEasemobException(Throwable throwable){
		super(throwable);
	}

	public RegisterEasemobException(String frdMessage,Throwable throwable){
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
