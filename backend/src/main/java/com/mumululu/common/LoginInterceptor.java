package com.mumululu.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mumululu.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(MAPPER.writeValueAsString(Result.fail(401, "未登录")));
            return false;
        }

        Claims claims = JwtUtil.parseToken(token);
        if (claims == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(MAPPER.writeValueAsString(Result.fail(401, "登录已过期，请重新登录")));
            return false;
        }

        // 将用户信息存入 request attribute，方便后续使用
        request.setAttribute("userId", Long.valueOf(claims.getSubject()));
        request.setAttribute("username", claims.get("username", String.class));
        request.setAttribute("realName", claims.get("realName", String.class));
        request.setAttribute("role", claims.get("role", Integer.class));
        return true;
    }
}
