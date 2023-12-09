package com.example.inkzone.config;

import com.example.inkzone.interceptors.GreetingInterceptor;
import com.example.inkzone.interceptors.BlackListIpInterceptor;
import com.example.inkzone.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    private  final UserService userService;

    public InterceptorConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(new GreetingInterceptor(userService)).addPathPatterns("/");
        interceptorRegistry.addInterceptor(new BlackListIpInterceptor());
    }

}
