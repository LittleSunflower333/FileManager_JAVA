package com.fileManager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your_secret_key"; // 替换为实际密钥
    private static final long EXPIRATION_TIME = 86400000; // Token 有效期 (1 天)

    /**
     * 生成 JWT Token
     *
     * @param userId 用户 ID
     * @return JWT Token
     */
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId) // 设置主体
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // 签名算法和密钥
                .compact();
    }

    /**
     * 验证 JWT Token 是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 无效 Token 或解析错误
        }
    }

    /**
     * 从 JWT Token 中提取用户 ID
     *
     * @param token JWT Token
     * @return 用户 ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
