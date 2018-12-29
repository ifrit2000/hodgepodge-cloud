package io.github.cd871127.hodgepodge.web;

import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.FAILED;


@RestControllerAdvice
public class WebControllerAdvise {
    @ExceptionHandler(Exception.class)
    public ServerResponse exceptionHandler(Exception e) {
        e.printStackTrace();
        return new ServerResponse(FAILED);
    }
}
