package com.mumululu.service;

import com.mumululu.common.BusinessException;
import com.mumululu.entity.SysUser;
import com.mumululu.mapper.UserMapper;
import com.mumululu.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private static final int MAX_LOGIN_FAIL = 5;
    private static final int LOCK_MINUTES = 30;

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Map<String, Object> login(String username, String password) {
        SysUser user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查账号是否锁定
        if (user.getStatus() == 0 && user.getLockedUntil() != null) {
            if (user.getLockedUntil().isAfter(LocalDateTime.now())) {
                throw new BusinessException("账号已被锁定");
            }
            // 锁定已过期，自动解锁
            user.setStatus(1);
            user.setLoginFail(0);
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            int failCount = (user.getLoginFail() == null ? 0 : user.getLoginFail()) + 1;
            if (failCount >= MAX_LOGIN_FAIL) {
                LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(LOCK_MINUTES);
                userMapper.updateLoginFail(user.getId(), failCount, 0,
                        lockedUntil.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                throw new BusinessException("账号已被锁定");
            }
            userMapper.updateLoginFail(user.getId(), failCount, user.getStatus(), null);
            throw new BusinessException("用户名或密码错误");
        }

        // 登录成功，重置失败次数
        userMapper.updateLoginFail(user.getId(), 0, 1, null);

        // 生成 token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername(), user.getRealName(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("role", user.getRole());
        return result;
    }

    public void logout(String token) {
        JwtUtil.invalidateToken(token);
    }

    public Map<String, Object> getUserInfo(String token) {
        Claims claims = JwtUtil.parseToken(token);
        if (claims == null) {
            throw new BusinessException(401, "未登录");
        }
        Map<String, Object> info = new HashMap<>();
        info.put("userId", Long.valueOf(claims.getSubject()));
        info.put("username", claims.get("username", String.class));
        info.put("realName", claims.get("realName", String.class));
        info.put("role", claims.get("role", Integer.class));
        return info;
    }
}
