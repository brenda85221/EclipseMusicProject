package team5.profile.tools;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 System.out.println("JWT Filter 正在處理請求: " + request.getMethod() + " " + request.getRequestURI());
		String authorizationHeader = request.getHeader("Authorization");

//		// 允許 multipart/form-data，不攔截
//        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7); // 移除 "Bearer " 部分

			try {
				Integer userId = JwtTool.getProfileIdFromToken(token); // 獲取用戶 ID
				Set<String> roles = JwtTool.getRoles(token); // 獲取角色集合
				String acct=JwtTool.getAcctFromToken(token);
				
//				System.out.println("JWT 解析成功！");
//	            System.out.println("解析出的使用者ID：" + userId);
//	            System.out.println("解析出的帳號：" + acct);
//	            System.out.println("解析出的角色：" + roles);
	            
				// 將角色轉換為 Spring Security 的 Authority
				var authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();
//				.collect(Collectors.toList());

				// 設置安全上下文
				var authentication = new UsernamePasswordAuthenticationToken(userId, acct, authorities);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				// 無效的 Token，清除上下文
				SecurityContextHolder.clearContext();
				System.err.println("Invalid JWT Token: " + e.getMessage()); // 修改：添加日誌輸出
			}
		}

		filterChain.doFilter(request, response); // 繼續處理請求
	}
}
