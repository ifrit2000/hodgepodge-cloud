package io.github.cd871127.hodgepodge.cloud.shiro;

import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

/**
 * @author Anthony Chen
 * @date 2019/10/29
 **/
public class ShiroApplication {
    public static void main(String[] args) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");


    }
}
