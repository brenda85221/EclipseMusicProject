package team5.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import team5.profile.tools.JwtAuthenticationFilter;
import team5.profile.tools.JwtTool;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//	private static final String[] PUBLIC_URLS = { "/profile/accountcheck.controller",  "/profile/login",  "/profile/verify-otp",
//			"/profile/send-otp" };
	private static final String[] PUBLIC_URLS = { "/profile/public/**", "/img/**","/profile/member/api/update/**","**" };

	private static final String[] ADMIN_URLS = { "/profile/admin/**" };

	private static final String[] USER_URLS = { "/profile/user/**" };

	private static final String[] MEMBER_URLS = { "/profile/member/**" };
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	
	@Autowired
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 禁用 CSRF
				.csrf(csrf -> csrf.disable())
				// 設定路徑授權規則
				.authorizeHttpRequests(auth -> auth
//						.requestMatchers("/profile/accountcheck.controller").permitAll() // 公共路徑允許匿名訪問
						.requestMatchers(PUBLIC_URLS).permitAll() // 公共路徑允許匿名訪問,允許預檢請求
						.requestMatchers("/ws/**").permitAll() // 允許 WebSocket 的匿名訪問
//	                    .requestMatchers(HttpMethod.PUT, "/api/update/**").permitAll() // 測試是否問題來自權限
						.requestMatchers(ADMIN_URLS).hasAuthority("Admin") // ADMIN 角色
						.requestMatchers(USER_URLS).hasAuthority("User") // USER 角色
						.requestMatchers(MEMBER_URLS).hasAnyAuthority("User", "Admin") // 允許所有會員
//						.requestMatchers(HttpMethod.PUT, "/profile/member/api/update/**").hasAnyAuthority("User", "Admin")
						.anyRequest().authenticated() // 其他請求需要驗證
				)

				// 設定無狀態的會話管理
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// 添加自定義的 JWT 認證過濾器(新)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

				// 將 CorsFilter 作為第一個過濾器
				.addFilterBefore(corsFilter(), JwtAuthenticationFilter.class);// CORS 應該放在 JWT 認證前

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Spring Security 的 CORS 配置
	// 設定 CORS 允許 `multipart/form-data`
	@Bean
	public CorsFilter corsFilter() { // 處理跨域請求
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174","http://localhost:5175","http://localhost:5176","https://chenyu-musicweb.netlify.app","https://brendamanagemusicweb.netlify.app")); // 指定允許的跨域來源
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 允許的自定義標頭
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin")); // 可以被接受請求
		configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type")); // 可以被客戶端訪問響應
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return new CorsFilter(source);
	}
}

//拼接路徑並設置允許訪問
//http
// .authorizeHttpRequests()
// .requestMatchers(
//     Arrays.stream(PUBLIC_URLS)
//           .map(url -> "/profile" + url)  // 拼接 "/profile" 前綴
//           .toArray(String[]::new)        // 轉換為陣列
// ).permitAll()
// .anyRequest().authenticated();