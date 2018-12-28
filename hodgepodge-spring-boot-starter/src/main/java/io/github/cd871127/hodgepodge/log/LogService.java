package io.github.cd871127.hodgepodge.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public interface LogService {
    void before(JoinPoint joinPoint);

    void after(JoinPoint joinPoint);

    void afterReturning(Object ret);

    Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;
}
