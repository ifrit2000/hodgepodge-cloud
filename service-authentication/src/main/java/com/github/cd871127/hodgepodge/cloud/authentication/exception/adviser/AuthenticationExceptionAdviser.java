package com.github.cd871127.hodgepodge.cloud.authentication.exception.adviser;

import com.github.cd871127.hodgepodge.cloud.authentication.exception.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Component
@ControllerAdvice
public class AuthenticationExceptionAdviser {
    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public String handException(HttpServletRequest request, Exception e) throws Exception {
        e.printStackTrace();
        return "123123123123";
    }
}
