package com.mumululu.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JwtUtil {

    private static final String SECRET = "mumululu-jwt-secret-key-2024-0123456789abcdef";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(SECRET.getBytes()).getBytes());
    private static final long EXPIRE_MS = 24 * 60 * 60 * 1000L;

    private static final ConcurrentHashMap<String, Long> BLACKLIST = new ConcurrentHashMap<>();

    public static String generateToken(Long userId, String username, String realName, Integer role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .claim("realName", realName)
                .claim("role", role)
                .setIssuedAt(new java.util.Date(now))
                .setExpiration(new java.util.Date(now + EXPIRE_MS))
                .signWith(KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        cleanExpired();
        if (BLACKLIST.containsKey(token)) {
            return null;
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public static void invalidateToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null && claims.getExpiration() != null) {
            BLACKLIST.put(token, claims.getExpiration().getTime());
        }
    }

    public static boolean isTokenValid(String token) {
        return parseToken(token) != null;
    }

    private static void cleanExpired() {
        long now = System.currentTimeMillis();
        BLACKLIST.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}
