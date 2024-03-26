package com.penny.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 配置安全過濾器鏈以及相關的安全設置。
     *
     * @param http HttpSecurity 物件，用於配置安全性。
     * @return 配置好的安全過濾器鏈。
     * @throws Exception 如果在配置過程中發生異常。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    // 關閉 CSRF 保護，以避免錯誤。不應在生產環境中使用
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        // 對 "/login/**" 和 "/register/**" 請求進行授權，允許所有人訪問
                        req -> req.requestMatchers("/api/v1/auth/login","/api/v1/auth/register", "/error")
                                .permitAll()
                                // 對於任何其他請求：需要身份驗證
                                .anyRequest()
                                .authenticated()
                )
                // 設置自定義的 UserDetailsService 來載入使用者的詳細資訊物件
                .userDetailsService(userDetailsServiceImpl)
                // 將 JWT 認證過濾器添加到 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 設置 session 管理策略為 STATELESS，即無狀態
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * 返回 BCryptPasswordEncoder 實例，用於加密密碼。
     *
     * @return BCryptPasswordEncoder 實例。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 根據 AuthenticationConfiguration 配置返回身份驗證管理器。
     *
     * @param configuration AuthenticationConfiguration 配置。
     * @return 身份驗證管理器。
     * @throws Exception 如果在配置過程中發生異常。
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
