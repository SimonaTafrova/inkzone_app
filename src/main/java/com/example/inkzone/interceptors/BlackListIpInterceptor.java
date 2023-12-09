package com.example.inkzone.interceptors;


import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class BlackListIpInterceptor implements HandlerInterceptor {
    private List<String> blacklistedIps = new ArrayList<>();

    public BlackListIpInterceptor() {
        blacklistedIps.add("0:0:0:0:0:0:0:0");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getRemoteAddr();
        if(blacklistedIps.contains(ipAddress)) {
            response.sendRedirect("error");
        }
        return true;
    }
}
