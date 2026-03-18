package team5.profile.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import team5.profile.model.service.EmailService;
import team5.profile.model.service.ProfilesService;
import team5.profile.tools.JwtTool;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/profile/public")
public class MailController {

	@Autowired
	private EmailService eService;

	@Autowired
	private ProfilesService pService;

	@Value("${verification.url}") // 從 application.properties 讀取配置
	private String verificationUrl;

	// 註冊會員 - 發送驗證郵件
	@PostMapping("/register")
	public ResponseEntity<Void> sendRegistrationMail(@RequestParam("email") String email) throws MessagingException {

		// 驗證 email 是否為空
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email is required");
		}
		// 生成一次性驗證 token
		String token = JwtTool.generateOneTimeToken(email);
		// 建立驗證連結
		String verifyLink = verificationUrl + "#/verify?token=" + token;
		// 設定郵件內容
		String emailContent = "<p>Dear user,</p><p>Click the link below to confirm your registration:</p>" + "<a href='"
				+ verifyLink + "'>Verify Email</a>";
		// 發送 HTML 郵件
		eService.sendHtmlMail(email, "註冊驗證", emailContent);
		return ResponseEntity.noContent().build();
	}

	// 忘記密碼 - 發送重設密碼郵件
	@PostMapping("/forgot-password")
	public ResponseEntity<Void> sendForgotPasswordMail(@RequestParam String email) throws MessagingException {
		String token = JwtTool.generateOneTimeToken(email);

		String resetLink = verificationUrl + "#/reset-password?token=" + token;

		String emailContent = "<p>Dear user,</p><p>Click the link below to reset your password:</p>" + "<a href='"
				+ resetLink + "'>Reset Password</a>";

		eService.sendHtmlMail(email, "重設密碼", emailContent);
		return ResponseEntity.noContent().build();
	}

	// 驗證一次性 Token
	@PostMapping("/verify-token")
	public ResponseEntity<String> verifyToken(@RequestParam String token) {
		try {
			// 1. 驗證 Token 並取得 Email
			String email = JwtTool.validateOneTimeToken(token);
			// 2. 查詢用戶資料並更新帳號狀態為啟用
			boolean isUpdated = pService.activateAccount(email); // 啟用帳號
			if (isUpdated) {
				return ResponseEntity.ok("Account activated successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account activation failed.");
			}
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	// 重設密碼
	@PostMapping("/reset-password")
	public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
		try {
			String email = JwtTool.validateOneTimeToken(token);
			eService.updatePasswordByEmail(email, newPassword);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

}

/*
 * //重設密碼連結
 * 
 * @PostMapping("/mail") public ResponseEntity<Void> sendHtmlMail(@RequestBody
 * SendMailRequest request) throws MessagingException { MimeMessage message =
 * sender.createMimeMessage(); MimeMessageHelper helper = new
 * MimeMessageHelper(message, true);
 * 
 * helper.setFrom(String.format("%s<%s>",displayName, username));
 * helper.setTo(request.getReceivers());
 * helper.setSubject(request.getSubject());
 * 
 * // 使用 HTML 格式設置內容 String content = String.format(
 * "<p>Dear user,</p><p>Click to reset your password</p>" +
 * "<a href='%s'>change password</a>", "http://localhost:5173/#/login" ); // 啟用
 * HTML 格式化 helper.setText(content, true);
 * 
 * sender.send(message); return ResponseEntity.noContent().build(); }
 */