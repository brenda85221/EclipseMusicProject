package team5.profile.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import team5.profile.model.ProfilesRepository;
import team5.profile.model.bean.ProfilesBean;
import team5.profile.model.bean.RolesBean;
import team5.profile.model.dto.LoginRequest;
import team5.profile.model.service.AuthService;
import team5.profile.model.service.ProfilesService;
import team5.profile.tools.JwtTool;

@RestController
@Transactional
//@CrossOrigin(origins = "*", allowCredentials = "true")
@RequestMapping("/profile/public")
public class Login {

	@Autowired
	private ProfilesService pService;

	@Autowired
	private AuthService authService;

	@PostMapping("/accountcheck.controller")
	public ResponseEntity<Object> processAction(@RequestBody Map<String, Object> requestBody,
			HttpServletRequest request) {
		System.out.println("Received request for account check: " + requestBody);
		
//		// 從請求中提取資料
//		String acct = requestBody.get("acct") != null ? (String) requestBody.get("acct") : "";
//		String pwd = requestBody.get("pwd") != null ? (String) requestBody.get("pwd") : "";
//		boolean rememberMe = Boolean.parseBoolean(String.valueOf(requestBody.getOrDefault("rememberMe", false)));
		
		// 從請求中提取數據
        String acct = (String) requestBody.getOrDefault("acct", "");
        String pwd = (String) requestBody.getOrDefault("pwd", "");
        boolean rememberMe = Boolean.parseBoolean(String.valueOf(requestBody.getOrDefault("rememberMe", false)));
       
        if (acct.isBlank() || pwd.isBlank()) {
//        	return ResponseEntity.status(400).body(Map.of("error", "Missing account or password"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing account or password"));
        }
		ProfilesBean user = new ProfilesBean();
		user.setAcct(acct);
		user.setPwd(pwd);

		// 驗證帳號密碼
		boolean status = pService.checkLogin(user);
		if (!status) {
//			return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials", "code", "N"));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials", "code", "N"));
		}

		// 查詢用戶資訊
		ProfilesBean exist = pService.getUserByAccountAndPassword(user.getAcct(), user.getPwd());
		if (exist == null) {
		    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials", "code", "N"));
		}

		if (exist.getAcctStatus() == 9) {
		    // 帳號未啟用
		    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Account not activated", "code", "Z"));
		}

		if (exist.getAcctStatus() == 0) {
		    // 帳號已停用
		    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Account inactive", "code", "X"));
		}

		// 根據角色生成權限集合
        String roleName = exist.getRolesBean().getRoleName();
        Set<String> roles = Set.of(roleName);
		
		// 動態生成 Token
		try {
		    String token = authService.generateTokenWithRememberMe(String.valueOf(exist.getProfileId()), rememberMe, exist.getAcct(), roleName);
//			String token = authService.generateTokenWithRememberMe(String.valueOf(exist.getProfileId()), rememberMe);
			System.out.println("Generated Token: " + token);
			// 使用 Map 封裝返回數據
			Map<String, Object> response = new HashMap<>();
			response.put("status", "success");
			response.put("data", Map.of("username", exist.getUserName(),"useracct", exist.getAcct(), "shotPath", exist.getShot(), "userId",
					exist.getProfileId(), "jwtToken", token, "isLoggedIn", true, "roles",roles, "rememberMe", rememberMe));

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			System.err.println("Error generating token: " + e.getMessage());
			e.printStackTrace(); // 打印堆棧信息以供調試
//			return ResponseEntity.status(500).body(Map.of("error", "Failed to generate token", "details", e.getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to generate token"));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
			@RequestParam(required = false, defaultValue = "false") boolean rememberMe) {
		// 驗證登錄數據
		ProfilesBean profile = pService.getUserByAccountAndPassword(loginRequest.getAcct(), loginRequest.getPwd());
		if (profile == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid account or password"));//("帳號或密碼錯誤");
		}
		if (profile.getAcctStatus() == 0) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Account has been disabled"));//("帳號已停用");
		}
		if (profile.getAcctStatus() == 9) {  // 檢查帳號是否已啟用
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Account not activated, please check your email"));
	    }
		// 提取角色名稱並生成權限集合
		String roleName = profile.getRolesBean().getRoleName();
        Set<String> roles = Set.of(roleName);
        
		// 生成 JWT
		String token = authService.generateTokenWithRememberMe(String.valueOf(profile.getProfileId()), rememberMe, profile.getAcct(), roleName);

		// 返回 Token 和用戶信息
		return ResponseEntity.ok(Map.of(
				"success", true,
                "jwtToken", token,
                "useracct", profile.getAcct(),
                "profileId", profile.getProfileId(),
                "username", profile.getUserName(),
                "roles", roles,
                "rememberMe", rememberMe));
	}
	/*
	// 發送密碼重設連結
	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    ProfilesBean user = pService.findByEmail(email);

	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查無信箱");
	    }

	    String token = UUID.randomUUID().toString();
	    user.setVerificationToken(token);
	    pService.save(user);

	    String resetLink = "http://localhost:8080/profile/public/api/resetPassword?token=" + token;
	    emailService.sendResetPasswordEmail(email, resetLink);

	    return ResponseEntity.ok("密碼重設連結已發送到您的信箱");
	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
	    String token = request.get("token");
	    String newPassword = request.get("newPassword");

	    ProfilesBean user = pService.findByVerificationToken(token);
	    if (user == null) {
	        return ResponseEntity.badRequest().body("無效的重設連結");
	    }

	    user.setPwd(newPassword);
	    user.setVerificationToken(null);
	    pService.save(user);

	    return ResponseEntity.ok("密碼已重設，請重新登入");
	}
	*/
}