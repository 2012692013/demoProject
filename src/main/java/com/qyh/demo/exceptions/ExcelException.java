package com.qyh.demo.exceptions;

/**
 * @author Qiu_yh
 * @date 2022/9/6 15:58
 */
public class ExcelException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 2971985159050386791L;


    public ExcelException(String frdMessage)	{
        super(createFriendlyErrMsg(frdMessage));
    }

    public ExcelException(String frdMessage,String mes)	{
        super(frdMessage);
    }

    public ExcelException(Throwable throwable){
        super(throwable);
    }

    public ExcelException(String frdMessage,Throwable throwable){
        super(createFriendlyErrMsg(frdMessage),throwable);
    }

    private static String createFriendlyErrMsg(String msgBody) {
//		String prefixStr = "抱歉，";
//		String suffixStr = " 请稍后再试或与管理员联系！";
//		StringBuffer friendlyErrMsg = new StringBuffer("");
//		friendlyErrMsg.append(prefixStr);
//		friendlyErrMsg.append(msgBody);
//		friendlyErrMsg.append(suffixStr);
//		return friendlyErrMsg.toString();
        return msgBody;
    }
}
