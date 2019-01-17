package io.github.cd871127.hodgepodge.log;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
@Data
@Slf4j
public class ControllerLogger {

    private LogService logService;

    public ControllerLogger() {
    }

    public ControllerLogger(LogService logService) {
        this();
        setLogService(logService);
    }

    /**
     * 切点
     * RestController和Controller注解的类下所有被RequestMapping修饰的方法
     */
    @Pointcut("(@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller))&&(" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            ")")
    public void controller() {
    }

    @Before("controller()")
    public void before(JoinPoint joinPoint) {
        logService.before(joinPoint);

    }

    @After("controller()")
    public void after(JoinPoint joinPoint) {
        logService.after(joinPoint);
    }

    @AfterReturning(returning = "ret", pointcut = "controller()")
    public void afterReturning(Object ret) {
        logService.afterReturning(ret);
    }

    @Around("controller()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return logService.around(proceedingJoinPoint);
    }
}
