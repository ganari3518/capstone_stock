package com.syu.capstone_stock.interceptor;

import com.syu.capstone_stock.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Member m = (Member) session.getAttribute("loginMember");
        if (m == null || m.getDeleteYn() == true) {
            response.sendRedirect("/login.do");
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
