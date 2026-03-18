package team5.profile.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import team5.profile.model.ProfilesRepository;
import team5.profile.model.bean.ProfilesBean;

@Service
public class EmailService {

	@Autowired
	private ProfilesRepository pDao;

//	@Autowired
//	private PasswordEncoder passwordEncoder; // 用於加密新密碼

	@Autowired
	private JavaMailSender sender;

	// 寄信email
	@Value("${spring.mail.username}") // properties
	private String username;

	// 寄信人
	@Value("${mail.display-name}")
	private String displayName;

	// 通用的發送 HTML 郵件方法
	public void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(String.format("%s<%s>", displayName, username));
		helper.setTo(to);
		helper.setSubject(subject);

		// 設置 HTML 格式內容
		helper.setText(htmlContent, true);

		sender.send(message);
	}

	// 更新密碼
	public void updatePasswordByEmail(String email, String newPassword) {
		// 查找用戶
		ProfilesBean user = pDao.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));

//		// 加密新密碼
//		String encodedPassword = passwordEncoder.encode(newPassword);

		// 更新用戶密碼
//		user.setPwd(encodedPassword);
		user.setPwd(newPassword);

		// 保存到資料庫
		pDao.save(user);
	}

}
