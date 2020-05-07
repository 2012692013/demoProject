package com.qyh.demo.base.handler;

import com.qyh.demo.base.util.Exceptions;
import com.qyh.demo.base.util.Kit;
import com.qyh.demo.base.util.StringUtils;
import com.qyh.demo.entity.SysException;
import com.qyh.demo.service.SysExceptionService;
import com.qyh.demo.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 异常处理
 * @author hx
 * 2018年6月12日
 */
@RestControllerAdvice
public class SysExceptionHandler {

	@Autowired
	private SysExceptionService seSer;



	@ExceptionHandler(Exception.class)
	public ResponseResult exceptionHandle(HttpServletRequest request,HttpServletResponse response,Exception e){
		String msg = "";
		SysException se = new SysException();
		se.setCreateTime(new Date());
		se.setInfo(Exceptions.getStackTraceAsString(e));
		se.setName(e.getClass().getName());
		se.setIp(StringUtils.getRemoteAddr(request));
		se.setMethod(request.getRequestURL().toString());
		se.setParam(Kit.getParamStr(request.getParameterMap()));
		se.setWay(request.getMethod());
		se.preInsert(request);
		String exceptionStr = e.toString();
		if(!exceptionStr.contains("HttpMediaTypeNotAcceptableException")){
			seSer.saveEntity(se);
			e.printStackTrace();
		}

		if(exceptionStr.contains("BusinessException")){
			System.out.println("======业务错误=====");
			msg = "未知错误，系统异常";
		}else if(exceptionStr.contains("HttpRequestMethodNotSupportedException")){
			System.out.println("======请求方式错误======");
			msg = "请求方式错误，请用正确的方式请求";
		}else if(exceptionStr.contains("JsonMappingException")){
			System.out.println("======json映射转换异常======");
			msg = "json映射转换异常";
		}else if(exceptionStr.contains("ConcurrenceException")){
			System.out.println("======并发错误======");
			msg = "并发错误,数据已被修改";
		}else if(exceptionStr.contains("SysCodeException")){
			System.out.println("======系统参数未配置，异常======");
			msg = "系统参数未配置，异常";
		}else if(exceptionStr.contains("IllegalArgumentException")){
			System.out.println("======非法参数，异常======");
			msg = "非法参数，异常";
		}else if(exceptionStr.contains("NumberFormatException")){
			System.out.println("======数字转换，异常======");
			msg = "数字转换，异常";
		}else if(exceptionStr.contains("BindException")){
			System.out.println("======参数绑定，异常======");
			msg = "参数绑定，异常";
		}else if(exceptionStr.contains("SendMessageEmailException")){
			System.out.println("======邮件发送，异常======");
			msg = "邮件发送，异常";
		}else if(exceptionStr.contains("SMSException")){
			System.out.println("======短信发送，异常======");
			msg = "短信发送，异常";
		}else if(exceptionStr.contains("SMSException")){
			System.out.println("======短信发送，异常======");
			msg = "短信发送，异常";
		}else if(exceptionStr.contains("PushException")){
			System.out.println("======推送，异常======");
			msg = "推送，异常";
		}else if(exceptionStr.contains("ParamException")){
			System.out.println("======参数值，错误======");
			msg = "参数值，错误";
		}else if(exceptionStr.contains("EasemobMsgLogException")){
			System.out.println("======下载环信消息记录，错误======");
			msg = "下载环信消息记录，错误";
		}else if(exceptionStr.contains("NullPointerException")){
			System.out.println("======  空数据，错误======");
			msg = "查不到相关记录，错误";
		}else if(exceptionStr.contains("RegisterEasemobException")){
			System.out.println("======  环信注册，失败======");
			msg = "环信注册，失败";
		}else if(exceptionStr.contains("HttpMediaTypeNotAcceptableException")){
			System.out.println("======地址找不到"+request.getRequestURL()+"======");
			msg = "地址找不到，错误";
		}else if(exceptionStr.contains("DuplicateKeyException")){
			System.out.println("======主键重复======");
			msg = "已存在,请勿重复添加";
		}else {
			System.out.println("============系统发生未知异常啦！===============");
			msg = "系统开小差去了...";
		}

		return ResponseResult.error(500,msg);
	}

}
