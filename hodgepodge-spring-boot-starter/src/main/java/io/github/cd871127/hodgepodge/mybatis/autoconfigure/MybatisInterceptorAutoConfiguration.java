package io.github.cd871127.hodgepodge.mybatis.autoconfigure;

import io.github.cd871127.hodgepodge.mybatis.SqlLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisInterceptorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }
}
