package team5.profile.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	private String acct; // 用戶名或賬號
	private String pwd; // 密碼

	// 可以選擇覆寫 toString 方法，方便日誌調試
	@Override
	public String toString() {
		return "LoginRequest{" + "acct='" + acct + '\'' + ", pwd='" + pwd + '\'' + '}';
	}
}
