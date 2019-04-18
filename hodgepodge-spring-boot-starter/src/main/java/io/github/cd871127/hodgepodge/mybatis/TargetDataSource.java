package io.github.cd871127.hodgepodge.mybatis;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TargetDataSource {
    String value();
}
