package org.example.system.common.config;

import jakarta.annotation.Resource;
import org.example.system.common.interceptor.UserLoginAuthInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class UserWebMvcConfiguration implements WebMvcConfigurer {
    @Resource
    private UserLoginAuthInterceptor userLoginAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginAuthInterceptor)
                .addPathPatterns("/api/**");
    }
}
