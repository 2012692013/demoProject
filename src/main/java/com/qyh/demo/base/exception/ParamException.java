package com.qyh.demo.base.exception;

/**
 * 前端参数值 传递错误
 * @author qiuyuehao
 *
 */
public class ParamException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971985159050386791L;
	
	public ParamException(String frdMessage)	{
		super(createFriendlyErrMsg(frdMessage));
	}
	
	public ParamException(String frdMessage,String mes)	{
		super(frdMessage);
	}

	public ParamException(Throwable throwable){
		super(throwable);
	}

	public ParamException(String frdMessage,Throwable throwable){
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
