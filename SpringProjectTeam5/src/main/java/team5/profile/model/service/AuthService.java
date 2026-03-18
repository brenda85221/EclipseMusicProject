package team5.profile.model.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import team5.profile.tools.JwtTool;

@Service
public class AuthService {

    // 產生帶有 "記住我" 選項的 Token
	public String generateTokenWithRememberMe(String userId, boolean rememberMe, String acct, String role) {
		// 設置到期時間，記住我則延長到 30 天，否則為 1 小時
        int expiration = rememberMe ? 30 * 24 * 60 * 60 : JwtTool.getDefaultExpiration();

     // 創建角色集合，為單一角色提供支援
        Set<String> roles = new HashSet<>();
        roles.add(role);

        // 使用 JwtTool 生成 token
        return JwtTool.generateToken(userId, acct, roles, expiration);
    }
}

//SecretKey key = Keys.hmacShaKeyFor("MyVerySuperSecurityKeyForGenerateJwtToken".getBytes());
//return Jwts.builder()
//        .subject(userId)
//        .claim("username", username) // 添加帳號
//        .claim("role", role)         // 添加角色
//        .issuedAt(new Date())
//        .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
//        .signWith(key)
//        .compact();
//}