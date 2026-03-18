package team5.profile.model.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import team5.profile.model.bean.RolesBean;

@Getter
@Setter
public class ProfilesDTO {
	private Integer profileId;
	private String acct;
	private String pwd;
	private String userName;
	private String email;
	private String phone;
	private String address1;
	private String gender;
	private String shot;
	private Date birth;
	private String roleName;
	private Integer acctStatus;
}
