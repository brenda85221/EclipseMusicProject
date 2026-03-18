package team5.profile.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import team5.profile.model.ProfilesRepository;
import team5.profile.model.RolesRepository;
import team5.profile.model.bean.ProfilesBean;
import team5.profile.model.bean.RolesBean;

@Service
@Transactional
public class ProfilesService {

	@Autowired
	private ProfilesRepository pDao;

	@Autowired
	private RolesRepository rDao;

	public boolean checkLogin(ProfilesBean profilesBean) {
		ProfilesBean resultBean = pDao.findByAcctAndPwd(profilesBean.getAcct(), profilesBean.getPwd());

		if (resultBean != null) {
			return true;
		}
		return false;
	}

	public ProfilesBean getUserByAccountAndPassword(String acct, String pwd) {
		// 返回完整的用戶資訊
		return pDao.findByAcctAndPwd(acct, pwd);
	}

	public ProfilesBean insert(ProfilesBean pBean) {
		return pDao.save(pBean);
	}

	public List<ProfilesBean> findAll() {
		return pDao.findAll();
	}

	public ProfilesBean findOne(String acct) {
		return pDao.findByAcct(acct);
	}

	public void deleteByAcct(String acct) {
		ProfilesBean profile = pDao.findByAcct(acct); // 根據帳號查找會員
		if (profile != null) {
			pDao.delete(profile);
		} else {
			throw new IllegalArgumentException("找不到對應帳號的會員");
		}
	}

	public ProfilesBean update(ProfilesBean pBean) {
		return pDao.save(pBean);
	}

	public Page<ProfilesBean> findAllByPage(Pageable pageable) {
		return pDao.findAll(pageable);
	}

	/**
	 * 分配角色給用戶
	 * 
	 * @param acct     用戶帳號
	 * @param roleName 角色名稱
	 * @return 更新後的 ProfilesBean
	 */
	public ProfilesBean assignRoleToProfile(String acct, String roleName) {
		ProfilesBean profile = pDao.findByAcct(acct);
		RolesBean role = rDao.findByRoleName(roleName);

		if (profile == null) {
			throw new IllegalArgumentException("找不到對應帳號的會員：" + acct);
		}

		if (role == null) {
			throw new IllegalArgumentException("找不到對應名稱的角色：" + roleName);
		}

		// 設定角色到用戶
		profile.setRolesBean(role);
		return pDao.save(profile);
	}

	/**
	 * 新增用戶並指定角色
	 * 
	 * @param pBean    用戶資料
	 * @param roleName 角色名稱
	 * @return 新增後的 ProfilesBean
	 */
	public ProfilesBean insertWithRole(ProfilesBean pBean, String roleName) {
		RolesBean role = rDao.findByRoleName(roleName);

		if (role == null) {
			throw new IllegalArgumentException("找不到對應名稱的角色：" + roleName);
		}

		pBean.setRolesBean(role);
		return pDao.save(pBean);
	}

	/**
	 * 查詢用戶的角色
	 * 
	 * @param acct 用戶帳號
	 * @return 用戶的角色名稱
	 */
	public String findRolesByAcct(String acct) {
		ProfilesBean profile = pDao.findByAcct(acct);

		if (profile == null) {
			throw new IllegalArgumentException("找不到對應帳號的會員：" + acct);
		}

		RolesBean role = profile.getRolesBean();
		if (role == null) {
			return "該用戶未分配角色";
		}

		return role.getRoleName();
	}
	
	public boolean activateAccount(String email) {
	    // 查詢用戶資料
	    Optional<ProfilesBean> userOptional = pDao.findByEmail(email);
	    
	    if (userOptional.isPresent()) {
	    	ProfilesBean user = userOptional.get();
	        
	        // 確保帳號未啟用前才進行更新
	        if (user.getAcctStatus() == 9) { // 假設 0 是未啟用狀態
	            user.setAcctStatus(1); // 設定為啟用（1）
	            pDao.save(user); // 保存更新後的用戶資料
	            return true;
	        }
	    }
	    return false; // 如果帳號不存在或已啟用，返回 false
	}

}

/*
 * public ProfilesBean findByProfileId(Integer profileId) { //
 * 模擬查詢用戶（實際應從數據庫獲取用戶信息） ProfilesBean user = new ProfilesBean();
 * user.setProfileId(profileId); user.setEmail("user@example.com"); // 模擬 Email
 * user.setPhone("+0975154106"); // 模擬手機號 return user; }
 */