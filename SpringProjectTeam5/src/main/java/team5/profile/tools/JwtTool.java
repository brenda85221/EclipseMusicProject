package team5.profile.tools;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;

@Component
public class JwtTool {

	// 到期時限(s)-登入 Token
	private static final Integer DEFAULT_EXPIRATION = 60 * 60;

	// 到期時限(s)-臨時 Token
	private static final Integer ONE_TIME_TOKEN_EXPIRATION = 30 * 60; // 30 分鐘

	// 密鑰
	private static final SecretKey KEY = Keys.hmacShaKeyFor("MyVerySuperSecurityKeyForGenerateJwtToken".getBytes()); // 密鑰字串建議從配置文件中提取

	// 取得預設到期時限-登入 Token
	public static Integer getDefaultExpiration() {
		return DEFAULT_EXPIRATION;
	}

	// 產生登入token
	public static String generateToken(String id, String acct, Set<String> roles, int expiration) {
		return Jwts.builder() // 使用流化的建構器
				.subject(id) // 設定主承載內容,通常是唯一識別id
				.claim("acct", acct) // 添加角色到 Claims
				.claim("roles", roles) // 添加角色到 Claims
//				.claim("roles", roles.stream()
//					    .map(role -> role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase())
//					    .collect(Collectors.toList())) //統一格式大寫開頭
				
				.issuedAt(new Date()) // 發行時間
				.expiration(new Date(System.currentTimeMillis() + expiration * 1000)) // 到期時間
				.signWith(KEY) // 密鑰簽名
				.compact(); // 產生出JWT token
	}

	// 產生一次性 Token
	public static String generateOneTimeToken(String id) {
		return Jwts.builder().subject(id).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + ONE_TIME_TOKEN_EXPIRATION * 1000)).signWith(KEY)
				.compact();
	}

	// 驗證一次性 Token
	public static String validateOneTimeToken(String token) {
		try {
			Claims claims = getClaims(token);
			return claims.getSubject(); // 返回 userId
		} catch (ExpiredJwtException e) {
			throw new RuntimeException("一次性 Token 已過期");
		} catch (JwtException e) {
			throw new RuntimeException("一次性 Token 無效");
		}
	}

	// 解析token
	private static Claims getClaims(String jwtToken) {
		return Jwts.parser() // 使用流化的建構器
				.verifyWith(KEY) // 驗證密鑰
				.build() // 建立解析器
				.parseSignedClaims(jwtToken) // 解析token
				.getPayload(); // 取得Claims
	}

	// 獲取ProfileId
	public static Integer getProfileIdFromToken(String jwtToken) {
		Claims claims = getClaims(jwtToken);
		return Integer.valueOf(claims.getSubject()); // 根據 subject 獲取 ProfileId
	}

	// 獲取用戶角色
	public static Set<String> getRoles(String jwtToken) {
		Claims claims = getClaims(jwtToken);
		// 確保 roles 不為 null，若為 null，則返回空集合
		List<String> roles = claims.get("roles", List.class);
		// 統一格式化角色
		Set<String> roleSet = roles == null ? new HashSet<>() :
	        roles.stream().map(role -> role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase()).collect(Collectors.toSet());
//		Set<String> roleSet = roles == null ? new HashSet<>() : new HashSet<>(roles);
//		return roleSet.stream().map(String::toLowerCase).collect(Collectors.toSet()); // 統一轉小寫
		return roleSet; // 不轉換大小寫
	}

	// 獲取帳號
	public static String getAcctFromToken(String jwtToken) {
		return getClaims(jwtToken).get("acct", String.class);
	}
}

/*
 * //獲取ProfileId public static Integer getProfileIdFromToken(String jwtToken) {
 * Claims claims = getClaims(jwtToken); try { return
 * Integer.parseInt(claims.getSubject()); // 確保 subject 為數字 } catch
 * (NumberFormatException e) { throw new
 * IllegalArgumentException("Invalid subject in JWT: must be an integer"); } }
 * 
 * // 獲取用戶角色 // 在解析 token 時檢查 roles 是否正確 public static Set<String>
 * getRoles(String jwtToken) { Claims claims = getClaims(jwtToken); Object
 * rolesObject = claims.get("roles"); if (rolesObject instanceof List<?>) {
 * List<?> rolesList = (List<?>) rolesObject; Set<String> rolesSet = new
 * HashSet<>(); for (Object role : rolesList) { if (role instanceof String) {
 * rolesSet.add((String) role); } } return rolesSet; }
 * 
 * return Collections.emptySet(); } //0116新增 generateToken2測試 public static
 * String getAcctFromToken(String jwtToken) { return
 * getClaims(jwtToken).get("acct", String.class); }
 */
