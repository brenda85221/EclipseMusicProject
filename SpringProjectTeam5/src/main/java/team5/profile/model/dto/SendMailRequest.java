package team5.profile.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SendMailRequest {

	private String[] receivers = new String[0];
	private String subject = "";
	private String content = "";
}
