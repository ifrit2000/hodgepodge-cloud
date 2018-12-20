package com.github.cd871127.hodgepodge.log;

import org.aspectj.lang.JoinPoint;

public interface LogService {
    void before(JoinPoint joinPoint);

    void after(JoinPoint joinPoint);

    void afterReturning(Object ret);
}
