[toc]

# hodgepodge-cloud

### service-cipher
加密服务

### service-zuul
路由,过滤请求

### service-authentication
认证服务

### service-safe-box
保存各种账号密码


#坑
springboot的jar包启动，只会把jar包添加到classpath，所以logging.config指定classpath:，而logback的配置文件在jar外部，那么无法加载
解决方案1 
制定logback日志文件，-DLogback.configurationFile=/path/to/Logback.xml，那么无法把日志打到logging.path里面
解决方案2 不指定classpath


gradle配置task的时候,两个
task test{
    project.version //配置阶段执行,拿不到子目录中的build.gradle的version
    doFirst{
    project.version //运行阶段执行,可以拿到的子目录中build.gradle的version
}
}
解决方案: 把task用project.afterEvaluate包起来
