package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || username.trim().isEmpty()) {
            return Result.fail("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.fail("密码不能为空");
        }
        Map<String, Object> data = userService.login(username.trim(), password);
        return Result.ok(data);
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null && !token.isEmpty()) {
            userService.logout(token);
        }
        return Result.ok();
    }

    @GetMapping("/user/info")
    public Result<?> userInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            return Result.fail(401, "未登录");
        }
        Map<String, Object> info = userService.getUserInfo(token);
        return Result.ok(info);
    }
}
