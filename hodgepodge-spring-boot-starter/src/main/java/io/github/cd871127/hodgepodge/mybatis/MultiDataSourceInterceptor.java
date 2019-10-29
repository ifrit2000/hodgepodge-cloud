package io.github.cd871127.hodgepodge.mybatis;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author anthonychen
 */
@Slf4j
@Intercepts(value = {
        @Signature(type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                        CacheKey.class, BoundSql.class})})
public class MultiDataSourceInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = null;
        for (Object o : invocation.getArgs()) {
            if (o instanceof MappedStatement) {
                mappedStatement = (MappedStatement) o;
                break;
            }
        }
        if (mappedStatement != null) {
            String signature = mappedStatement.getId();
            String className = signature.substring(0, signature.lastIndexOf("."));
            String methodName = signature.substring(signature.lastIndexOf(".") + 1);
            Class<?> clazz = Class.forName(className);
            TargetDataSource targetDataSource = clazz.getAnnotation(TargetDataSource.class);
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    TargetDataSource tmp = method.getAnnotation(TargetDataSource.class);
                    targetDataSource = tmp == null ? targetDataSource : tmp;
                    break;
                }
            }
            MultiDataSourceManager.setCurrentDataSource(targetDataSource == null ? null : targetDataSource.value());
        }

        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
