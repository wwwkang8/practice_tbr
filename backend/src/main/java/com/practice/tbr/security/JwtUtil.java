package com.practice.tbr.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@Slf4j
public class JwtUtil {
    
    @Value("${supabase.jwt.secret}")
    private String jwtSecret;
    
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("sub", String.class);
    }
    
    public String extractStoreId(String token) {
        Claims claims = extractAllClaims(token);
        // Supabase JWT에서 store_id를 찾는 여러 방법 시도
        String storeId = claims.get("store_id", String.class);
        if (storeId == null) {
            // user_metadata에서 찾기
            Object userMetadata = claims.get("user_metadata");
            if (userMetadata instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> metadata = (java.util.Map<String, Object>) userMetadata;
                storeId = (String) metadata.get("store_id");
            }
        }
        if (storeId == null) {
            // app_metadata에서 찾기
            Object appMetadata = claims.get("app_metadata");
            if (appMetadata instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> metadata = (java.util.Map<String, Object>) appMetadata;
                storeId = (String) metadata.get("store_id");
            }
        }
        // 기본값으로 user_id 사용 (개발용)
        if (storeId == null) {
            storeId = claims.get("sub", String.class);
        }
        return storeId;
    }
    
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
