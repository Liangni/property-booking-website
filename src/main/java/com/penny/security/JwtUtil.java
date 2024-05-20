package com.penny.security;

import com.penny.vo.base.EcUserBaseVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {
    private static final int EXPIRE_IN_MS = 24 * 60 * 60 * 1000;
    // 256-bit KEY GENERATOR: https://asecuritysite.com/encryption/plain
    private final String SECRET_KEY = "61d0a3a468ac9b5941b9fd74dc62bc232523b433bb4577328dc8a483cd5483ca";

    /**
     * 驗證 JWT 是否有效。
     *
     * @param token JWT 字串
     * @param user 使用者詳細資訊
     * @return 如果 JWT 有效並且與給定使用者相符，則返回 true；否則返回 false。
     */
    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 檢查 JWT 是否已過期。
     *
     * @param token JWT 字串
     * @return 如果 JWT 已過期，則返回 true；否則返回 false。
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 從 JWT 中提取使用者名稱。
     *
     * @param token JWT 字串
     * @return 從 JWT 中提取的使用者名稱。
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 從 JWT 中提取到期時間。
     *
     * @param token JWT 字串
     * @return 從 JWT 中提取的到期時間。
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
        // 等同
        // Claims claims = getClaims(token);
        // return claims.getExpiration();
    }

    /**
     * 從 JWT 中提取特定聲明。
     *
     * @param token    JWT 字串
     * @param resolver 解析器函數
     * @param <T>      聲明類型
     * @return 從 JWT 中提取的聲明。
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * 從 JWT 中提取所有聲明。
     *
     * @param token JWT 字串
     * @return 從 JWT 中提取的所有聲明。
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 生成 JWT 字串。
     *
     * @param username 使用者帳號
     * @return 生成的 JWT 字串。
     */
    public String generateToken(String username) {
        return Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_IN_MS))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * 獲取用於簽署 JWT 的密鑰。
     *
     * @return 用於簽署 JWT 的密鑰。
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes= Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
