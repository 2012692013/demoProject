package com.qyh.demo.base.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IDEA
 * author:huxi
 * Date:2019/4/19
 * Time:10:51 AM
 */
@Slf4j
public class StreamFilter  implements Filter {


    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String contentType = request.getContentType();
        HttpServletResponse resp = (HttpServletResponse) response;
        String method = req.getMethod();

        if ((contentType != null) && (contentType.startsWith("multipart/form-data")) && ("POST".equalsIgnoreCase(method))) {
            chain.doFilter(request,response);
        } else {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(req);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(resp);
            try {
                chain.doFilter(requestWrapper, responseWrapper);
            } finally {
                String requestBody = "";
                if(StringUtils.isNotBlank(req.getQueryString())){
                    requestBody = new String(req.getQueryString().getBytes(),"utf-8");
                }else{
                    requestBody = new String(requestWrapper.getContentAsByteArray(),"utf-8");
                }

                if(StringUtils.isNotBlank(contentType)&&"application/json".equals(contentType)){
                    log.info("================请求参数:"+requestBody.replaceAll("\n","").replaceAll("\t","")+"==================");
                    String responseBody = new String(responseWrapper.getContentAsByteArray(),"utf-8");
                    log.info("================返回结果:"+responseBody+"==================");
                }
                responseWrapper.copyBodyToResponse();
            }
        }
    }
    @Override
    public void destroy() {
    }
}
