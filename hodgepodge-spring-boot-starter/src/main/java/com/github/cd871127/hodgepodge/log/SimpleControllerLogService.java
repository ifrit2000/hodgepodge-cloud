package com.github.cd871127.hodgepodge.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 记录日志行为
 */
@Slf4j
public class SimpleControllerLogService implements LogService {

    @Override
    public void before(JoinPoint joinPoint) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String httpMethod = httpServletRequest.getMethod();
        String url = httpServletRequest.getRequestURI();
        log.info("\nStart handle request:{}\nURI: {}\nMETHOD: {}\nARGS:{}", joinPoint.toString(), url, httpMethod, Arrays.toString(joinPoint.getArgs()));
    }

    @Override
    public void after(JoinPoint joinPoint) {
        log.info("\nFinish handle request:{}\nTime cost:{}", joinPoint.toString(), 0);
    }

    @Override
    public void afterReturning(Object ret) {
        log.info("\nRequest result:{}", ret);
    }
}
