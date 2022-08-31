package com.qyh.demo.base.exception;

/**
 * 邮件发送异常
 * @author qiuyuehao
 *
 */
public class SendMessageEmailException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971985159050386791L;
	
	public SendMessageEmailException(String frdMessage)	{
		super(createFriendlyErrMsg(frdMessage));
	}
	
	public SendMessageEmailException(String frdMessage,String mes)	{
		super(frdMessage);
	}

	public SendMessageEmailException(Throwable throwable){
		super(throwable);
	}

	public SendMessageEmailException(String frdMessage,Throwable throwable){
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
