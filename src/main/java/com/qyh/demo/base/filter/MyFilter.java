package com.qyh.demo.base.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 * @author hx
 * 2018年6月12日
 */
@WebFilter
public class MyFilter implements Filter{
	
	private static Logger logger = LoggerFactory.getLogger(MyFilter.class);
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("----------------过滤器正在启动-----------------");
	}

	@Override
	public void destroy() {
		logger.info("----------------过滤器销毁-----------------");
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain fc) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		
		response.setHeader("Access-Control-Allow-Origin","*");//解决跨域问题
		response.setHeader("Access-Control-Allow-Credentials","true"); 
		response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers","x-requested-with,token,Authorization,api-version,timestamp,apiSecret");
		response.setHeader("Access-Control-Expose-Headers","imgCode");

		fc.doFilter(req, res);
	}

}
