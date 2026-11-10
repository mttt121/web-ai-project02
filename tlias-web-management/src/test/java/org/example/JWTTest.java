package org.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;




public class JWTTest {
    @Test
    public void testGenJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 10);
        claims.put("username", "itheima");

        SecretKey key = Keys.hmacShaKeyFor(
                "itheima-jwt-secret-key-123456789".getBytes(StandardCharsets.UTF_8)
        );

        String jwt = Jwts.builder()
                .signWith(key, Jwts.SIG.HS256)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 12 * 3600 * 1000))
                .compact();

        System.out.println(jwt);
    }

    @Test
    public void testParseJwt() {
        SecretKey key = Keys.hmacShaKeyFor(
                "itheima-jwt-secret-key-123456789".getBytes(StandardCharsets.UTF_8)
        );

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoiaXRoZWltYSIsImV4cCI6MTc5MzkxMTA2Nn0.1lqW6B4ZwLt3jCls9xnR-oaYguOn_4qRCV0ZJIUUyoY")
                .getPayload();

        System.out.println(claims);
    }
}