package kr.co.koreanmagic.web.db.mybatis.interceptor;

import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts ({
@Signature(type = Executor.class, method = "update",
			args = {MappedStatement.class, Object.class})
,@Signature(type = Executor.class, method = "query",
			args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
,@Signature(type = Executor.class, method = "query",
			args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
}) 
public class ExampleInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		Executor e;
		
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement)args[0];
		System.out.println("interceptor");
		for(Object obj : invocation.getArgs())
			System.out.println(obj.getClass());
		return null;
	}

	@Override
	public Object plugin(Object target) {
		target= Plugin.wrap(target, this);
		System.out.println(target);
		return target;
		//System.out.println(target);
		//return null;
	}

	@Override
	public void setProperties(Properties properties) {
		System.out.println(properties);
	}

}
