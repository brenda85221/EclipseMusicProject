package team5.profile.model.bean;

import java.sql.Date;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
@Component
@JsonIgnoreProperties(ignoreUnknown = true) //忽略未在 ProfilesBean 中定義的字段
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "profileId")
public class ProfilesBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	// 如果屬性名稱與欄位名稱不同，則必須使用 @Column(name = "欄位名稱")來對應欄位
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer profileId;
	
	@Column(unique = true, nullable = false)
	private String acct;
	private String pwd;
	private String userName;
	private String email;
	private String phone;
	private String address1;
	private String gender;
	private String shot;
	private Date birth;
	
//	private String roleName = "user";;
//	@JsonIgnore // 防止序列化時遞歸
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleName", referencedColumnName = "roleName", nullable = false)
//	@JsonManagedReference // 正向關聯，這是 "父" 端,保持單向序列化
	private RolesBean rolesBean = new RolesBean("User", "一般使用者"); // 預設為 user 角色;
	
	private Integer acctStatus = 9;
	
//	@OneToMany(mappedBy = "profilesBean", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonBackReference // ✅ 防止循環序列化
//    private Set<SomeOtherEntity> relatedEntities;
	
	public ProfilesBean(String acct, String pwd, String userName, String email, String phone, String address1,
			String gender, String shot, Date birth, RolesBean rolesBean, Integer acctStatus) {
		this.acct = acct;
		this.pwd = pwd;
		this.userName = userName;
		this.email = email;
		this.phone = phone;
		this.address1 = address1;
		this.gender = gender;
		this.shot = shot;
		this.birth = birth;
		this.rolesBean = rolesBean;
		this.acctStatus = acctStatus;
	}
	
	public ProfilesBean() {
        // 預設值設定
        if (this.rolesBean == null) {
            this.rolesBean = new RolesBean("user", "一般使用者"); // 如果 rolesBean 沒有被初始化，給予預設值
        }
        if (this.acctStatus == null) {
            this.acctStatus = 1; // 預設為帳號狀態為 1
        }
    }

	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public String getAcct() {
		return acct;
	}
	public void setAcct(String acct) {
		this.acct = acct;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getShot() {
		return shot;
	}
	public void setShot(String shot) {
		this.shot = shot;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public RolesBean getRolesBean() {
		return rolesBean;
	}
	public void setRolesBean(RolesBean rolesBean) {
		this.rolesBean = rolesBean;
	}
	public Integer getAcctStatus() {
		return acctStatus;
	}
	public void setAcctStatus(Integer acctStatus) {
		this.acctStatus = acctStatus;
	}
	
}