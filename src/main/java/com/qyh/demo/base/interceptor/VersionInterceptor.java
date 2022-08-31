package com.qyh.demo.base.interceptor;

import java.util.Properties;

import com.qyh.demo.base.BaseEntity;
import com.qyh.demo.base.exception.ConcurrenceException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * 乐观锁并发拦截处理
 * @author qiuyuehao
 * 2018年6月12日
 */

@Intercepts({@Signature(
		  type= Executor.class,
		  method = "update",
		  args = {MappedStatement.class,Object.class})})
public class VersionInterceptor implements Interceptor{

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] objs = invocation.getArgs();

		boolean flag = false;

		for(Object obj : objs) {
			if(obj instanceof BaseEntity){
				BaseEntity entity = (BaseEntity)obj;
				if(entity.getVersion()!=null){
					flag = true;
				}
			}
		}

		if(flag){

			for (Object obj : objs) {

				if(obj instanceof MappedStatement){
					MappedStatement ms = (MappedStatement)obj;
					if(ms.getId().contains("update")){
						Object res = invocation.proceed();
						if(Integer.parseInt(res.toString())==0){
							throw new ConcurrenceException("并发错误，你的数据已经被修改");
						}
						return res;
					}
				}
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {
		
	}

}
