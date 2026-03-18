//package team5.forum.config;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureException;
//import org.springframework.stereotype.Component;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.filter.OncePerRequestFilter;
//import team5.util.forum.JwtTestUtil;
//
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    //private static final String SECRET_KEY = "test_secret_key_with_32_bytes_length";  // 用來簽名的密鑰，應該存儲在安全的地方
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // 從 headers 中取得 Authorization 標頭
//        String authHeader = request.getHeader("Authorization");
//
//        // 日誌輸出檢查 Token 是否存在
//        System.out.println("Request URI: " + request.getRequestURI());
//        System.out.println("Authorization Header: " + authHeader);
//
//        // 如果沒有 Authorization 標頭或格式不正確，則繼續處理請求
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // 提取 JWT Token
//        String jwtToken = authHeader.substring(7);  // 去掉 "Bearer " 前綴
//        System.out.println("New Authorization Header: " + jwtToken);
//
//        try {
//            // 驗證 Token 是否有效，這裡簡單地解析一下 Token 的有效性
//            JwtTestUtil.getClaims(jwtToken);  // 如果 Token 無效會拋出異常
//        } catch (SignatureException e) {
//            e.printStackTrace();  // 輸出具體錯誤
//            // 如果驗證失敗，返回 401 Unauthorized
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token.");
//            return;  // 不再繼續執行後續過濾器
//        }
//
//        // 如果 Token 有效，繼續執行後續過濾器
//        filterChain.doFilter(request, response);
//    }
//}
//
//
//
//
//
