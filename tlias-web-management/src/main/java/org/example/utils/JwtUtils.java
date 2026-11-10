package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET = "itheima-jwt-secret-key-123456789";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_MS = 12 * 3600 * 1000;

    public static String generate(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(KEY, Jwts.SIG.HS256)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .compact();
    }

    public static Claims parse(String jwt) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}