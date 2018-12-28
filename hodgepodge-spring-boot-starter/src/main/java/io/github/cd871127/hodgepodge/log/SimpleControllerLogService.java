package io.github.cd871127.hodgepodge.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

/**
 * 记录日志行为
 */
@Slf4j
public class SimpleControllerLogService implements LogService {

    private String separator = "\n" + String.join("", Collections.nCopies(101, " "));
    private String beforeLogMessage;

    @PostConstruct
    private void init() {
        beforeLogMessage = "Request:{} {} {}" + separator + "{}";
    }

    @Override
    public void before(JoinPoint joinPoint) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String httpMethod = httpServletRequest.getMethod();
        String url = httpServletRequest.getRequestURI();
        log.info(beforeLogMessage
                , httpMethod, url, Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature());
    }

    @Override
    public void after(JoinPoint joinPoint) {
//        log.info("\nFinish handle request:{}\nTime cost:{}", joinPoint.toString(), 0);
    }

    @Override
    public void afterReturning(Object ret) {

    }

    @Override
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object o;
        try {
            long start = System.currentTimeMillis();
            o = proceedingJoinPoint.proceed();
            log.info("Time consuming:{}{}Request result:{}", System.currentTimeMillis() - start, separator, JSON.toJSONString(o));
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            throw throwable;
        }
        return o;
    }
}
