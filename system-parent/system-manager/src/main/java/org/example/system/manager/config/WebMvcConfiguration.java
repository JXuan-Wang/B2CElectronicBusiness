package org.example.system.manager.config;

import jakarta.annotation.Resource;
import org.example.system.manager.interceptor.LoginAuthInterceptor;
import org.example.system.manager.properties.UserProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Resource
    private LoginAuthInterceptor loginAuthInterceptor;
    @Resource
    private UserProperties userProperties;

    //拦截器注册
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor)
                .excludePathPatterns(userProperties.getNoAuthUrls())
                .addPathPatterns("/**");
    }


    //解决跨域请求错误的问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)               // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")           // 允许请求来源的域规则
                .allowedMethods("*")
                .allowedHeaders("*") ;                // 允许所有的请求头
    }
}
