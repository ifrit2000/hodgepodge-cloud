package io.github.cd871127.hodgepodge.cloud.shiro;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anthony Chen
 * @date 2019/10/29
 **/
@RestController
@RequestMapping("/test")
public class ShiroController {
    @GetMapping("test")
    public String test() {
        return "test";
    }

    @GetMapping("test2")
    public String test2() {
        return "test2";
    }
}
