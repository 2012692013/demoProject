package com.qyh.demo.base.interceptor;

import com.qyh.demo.base.util.JedisUtil;
import com.qyh.demo.base.util.Kit;
import com.qyh.demo.base.util.bucket.TouristUtil;
import com.qyh.demo.constants.ContextConstants;
import com.qyh.demo.enums.JedisIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;



/**
 * 拦截器
 * @author qiuyuehao
 * 2018年6月12日
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor{


	@Override
	public void afterCompletion(HttpServletRequest arg0,
								HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
						   Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();



		if (uri.contains("swagger")) {
			response.setStatus(200);
			return true;
		}

		String contentType = request.getHeader("content-type");
		String token = request.getHeader("token");
		String method = request.getMethod();

		if(!uri.contains("/demo/error")){

			log.info("=============请求路径：" + uri + "============");
			log.info("=============请求方式：" + method + "============");
			log.info("=============请求时间：" + new Date().toLocaleString() + "============");
			log.info("=============token："+(StringUtils.isBlank(token)?"无token":token)+"============");
			if (!(StringUtils.isNotBlank(contentType)&&contentType.contains("application/json"))) {
				log.info("=============请求参数:" + getParamString(request.getParameterMap()) + "=============");
			}
			log.trace(handler.toString());
		}else{
			response.setStatus(404);
			return true;
		}


		String reqMethod = request.getMethod();
		if ("OPTIONS".equals(reqMethod)) {
			response.setStatus(200);
			return false;
		}

		String[] urlArr = uri.split("/");
		if (urlArr.length <= 2) {
			response.setStatus(404);
			return false;
		}

		StringBuffer sb = new StringBuffer("/");
		for (int i = 2; i < urlArr.length; i++) {
			sb.append(urlArr[i]).append("/");
		}

		//静态资源放行 后台页面sys  上传文件uploadFile
		if(uri.contains("/demo/sys")||uri.contains("/demo/uploadFile/")){
			return true;
		}

		if (!ContextConstants.PROFILES_ACTIVE.equals("dev")) {
			if (!TouristUtil.isTouristResouce(uri,reqMethod)) {
				if (checkIflogin(request, "tokenCache")) {//未登录
					response.setStatus(401);
					return false;
				}
			}
			/**
			 * 加密验证
			 */
//			boolean pass = checkLegal(request);
//			if(!pass){
//				response.setStatus(CodeEnum.ILLEGAL.codeOf());
//				return false;
//			}
		}
		return true;
	}


	/**
	 * 解析参数(form-data)
	 * hx 2018年3月29日 下午3:32:10
	 *
	 * @param map
	 * @return
	 */
	private String getParamString(Map<String, String[]> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> e : map.entrySet()) {
			sb.append(e.getKey()).append("=");
			String[] value = e.getValue();
			if (value != null && value.length == 1) {
				sb.append(value[0]).append("\t");
			} else {
				sb.append(Arrays.toString(value)).append("\t");
			}
		}
		return sb.toString();
	}

	private String getJsonParamString(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	/**
	 * 检查是否登录
	 * hx 2018年3月29日 下午4:33:51
	 * @param request
	 * @return
	 */
	private boolean checkIflogin (HttpServletRequest request, String cacheName){
		//检查用户是否已登录
		String token = request.getHeader("token");
		if (token == null) {
			return true;
		}
		Object userObj = JedisUtil.getJedisUtil().getObject(token, JedisIndex.USER);
		if (userObj == null) {
			return true;
		}
		return false;
	}

	/**
	 * 接口加密验证
	 * 加密规则:MD5(MD5(timestamp+"qyh"))
	 * @author qiuyuehao
	 * @return
	 * @exception
	 * @date        2019/9/23 5:00 PM
	 */
	private boolean checkLegal(HttpServletRequest request){
		if(request.getHeader("timestamp") == null || request.getHeader("apiSecret") == null){
			return false;
		}
		String secret = request.getHeader("apiSecret");
		Long timestamp = Long.valueOf(request.getHeader("timestamp"));
		//验证时间戳是否在过去的一分钟内
		Long subTime = System.currentTimeMillis() - timestamp;
		if(subTime > 60000L){
			return false;
		}
		//判断加密字符
		String curSecret = Kit.getMD5Str(Kit.getMD5Str(timestamp+"qyh"));
		if(!curSecret.equals(secret)){
			return false;
		}
		return true;
	}


}
