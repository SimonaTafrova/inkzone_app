package com.example.inkzone.interceptors;

import com.example.inkzone.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GreetingInterceptor implements HandlerInterceptor {
    private final UserService userService;

    public GreetingInterceptor(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        String greetingName = null;
        String profilePicture = null;
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser"){
            greetingName = userService.getFullName(SecurityContextHolder.getContext().getAuthentication().getName());
            profilePicture = userService.getProfilePicture(SecurityContextHolder.getContext().getAuthentication().getName());


        }
        modelAndView.addObject("name",greetingName);
        modelAndView.addObject("profilePicture", profilePicture);
    }





}
