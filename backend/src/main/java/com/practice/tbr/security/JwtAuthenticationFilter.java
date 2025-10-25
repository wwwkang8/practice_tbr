package com.practice.tbr.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String token = authHeader.substring(7);
            
            // 간단한 admin 토큰 검증
            if (token.startsWith("admin-token-")) {
                // Store user info in request attributes for use in controllers
                request.setAttribute("userId", "00000000-0000-0000-0000-000000000001");
                request.setAttribute("storeId", "00000000-0000-0000-0000-000000000002");
                
                // Set authentication in security context
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken("admin-user-id", null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else if (jwtUtil.isTokenValid(token)) {
                String userId = jwtUtil.extractUserId(token);
                String storeId = jwtUtil.extractStoreId(token);
                
                // Store user info in request attributes for use in controllers
                request.setAttribute("userId", userId);
                request.setAttribute("storeId", storeId);
                
                // Set authentication in security context
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
}
