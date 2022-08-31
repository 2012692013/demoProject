package com.qyh.demo.base.filter;

import com.qyh.demo.base.util.bucket.QSwaggerContext;
import com.qyh.demo.constants.ContextConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author qiuyuehao
 * @date 2020/3/19 13:50
 * create by 2012692013@qq.com
 */
@WebFilter(filterName = "paramFilter", urlPatterns = "/*")//过滤器拦截所有请求
@Order(100)
public class ParamFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest wrapper = new RequestWrapper(request);
        filterChain.doFilter(wrapper, response);//将修改过的request(wrapper)放回
    }

    private static class RequestWrapper extends HttpServletRequestWrapper {

        private Map<String, String[]> params = new HashMap<String, String[]>();

        public RequestWrapper(HttpServletRequest request) {
            super(request);
            boolean isJson = false;
            String requestURI = request.getRequestURI();
            String method = request.getMethod();
            String key = requestURI+":"+method;
            List<String> valid = null;
            if ("POST".equals(method)
                    ||"PUT".equals(method))
                valid = QSwaggerContext.CONTENT_MAP.get(key);
            String header = request.getHeader("Content-Type");
            if (StringUtils.isNotBlank(header)
                    && header.contains("application/json"))
                isJson = true;

            Map<String, Object> paraObj = null;

            Enumeration enu=request.getParameterNames();
            while (enu.hasMoreElements()) {
                String paraName=(String)enu.nextElement();
                String parameter = request.getParameter(paraName);
                if (ContextConstants.PARAM_VALID
                        && valid != null
                        && !valid.isEmpty()
                        // 校验表单 [json需要流取参校验]
                        && !isJson) {
                    if (!valid.contains(paraName))
                        continue;
                }
                params.put(paraName, new String[]{parameter});
            }
        }

        @Override
        public String getParameter(String name) {
            String[]values = params.get(name);
            if(values == null || values.length == 0) {
                return null;
            }
            return StringEscapeUtils.escapeHtml4(values[0]);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            Vector<String> v = new Vector<String>();
            Set<Map.Entry<String, String[]>> entrySet = params.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {
                v.add(entry.getKey());
            }
            Enumeration<String> en =  v.elements();

            return v.elements();
        }

        @Override
        public String[] getParameterValues(String name) {
            return params.get(name);
        }
    }
}
