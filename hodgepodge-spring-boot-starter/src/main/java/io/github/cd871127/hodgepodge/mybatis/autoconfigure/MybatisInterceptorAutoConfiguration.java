package io.github.cd871127.hodgepodge.mybatis.autoconfigure;

import io.github.cd871127.hodgepodge.mybatis.SqlLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({MappedStatement.class, MapperMethod.class, Interceptor.class})
public class MybatisInterceptorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mybatis", value = "sql-log", havingValue = "true", matchIfMissing = true)
    SqlLogInterceptor sqlLogInterceptor() {

        return new SqlLogInterceptor();
    }


}

