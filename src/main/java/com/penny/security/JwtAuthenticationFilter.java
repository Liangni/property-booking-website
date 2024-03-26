package com.penny.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * 在內部執行過濾操作。
     *
     * @param request       HTTP 請求
     * @param response      HTTP 回應
     * @param filterChain   過濾器鏈
     * @throws ServletException 如果發生 Servlet 錯誤
     * @throws IOException      如果發生 IO 錯誤
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("doFilterInternal");
        // 檢查請求中是否包含 Authorization 標頭
        String authHeader = request.getHeader("Authorization");

        // 如果 Authorization 標頭不存在，或者其值不以 "Bearer " 開頭
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 讓請求繼續傳遞到下一個過濾器
            filterChain.doFilter(request, response);
            System.out.println("authHeader not exist, leave internal filter");
            return;
        }
        System.out.println("Internal Filter continue");
        // 從 Authorization 標頭中提取出 token
        String token = authHeader.substring(7);

        // 從 token 中提取出使用者名稱
        String username = jwtUtil.extractUsername(token);

        // 如果使用者名稱存在且安全上下文中的身份驗證為空
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 用使用者名稱獲取 userDetails 物件
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

            // 如果 token 是有效的
            if(jwtUtil.isValid(token, userDetails)) {

                // 創建身份驗證令牌
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // 設置身份驗證令牌的詳細信息
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 將身份驗證令牌設置到安全上下文中
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 繼續傳遞請求到下一個過濾器
        filterChain.doFilter(request, response);
    }

}
