package team5.profile.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import team5.profile.model.bean.ProfilesBean;
import team5.profile.model.dto.ProfilesDTO;
import team5.profile.model.service.ProfilesService;

@Controller
//@PreAuthorize("hasRole('user')")
//@RequestMapping("/profile/user")
//@CrossOrigin(origins = "*", allowCredentials = "true")
public class ProfileController {

	@Autowired
	private ProfilesService pService;

	@Value("${photo.storage.prefix}")
	private String storagePrefix; // 動態讀取配置的存儲路徑

	// http://localhost:8080/profile/api/

	// 新增會員資料
	@PostMapping(value = "/profile/public/api/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> insertProfile(@RequestPart("profile") ProfilesBean pBean, // 接收其他資料
			@RequestPart(value = "shot", required = false) MultipartFile shot) { // 接收圖片
		Map<String, Object> response = new HashMap<>();

		try {
			// 處理文件保存
			if (shot != null && !shot.isEmpty()) {
				String fileName = pBean.getAcct() + ".jpg";
				File uploadDir = new File(storagePrefix + "/profShot");
				if (!uploadDir.exists()) {
					uploadDir.mkdirs(); // 如果目錄不存在，創建目錄
				}
				File uploadFile = new File(uploadDir, fileName);
				shot.transferTo(uploadFile); // 保存文件到目錄
				pBean.setShot("img/profShot/" + fileName); // 設置圖片相對路徑
			}

			// 插入資料到數據庫
			ProfilesBean savedBean = pService.insert(pBean);

			response.put("success", true);
			response.put("message", "會員資料新增成功");
			response.put("data", savedBean);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "新增資料時發生錯誤: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 確認帳號是否存在
	@PostMapping("/profile/public/api/checkAcct")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> checkAccount(@RequestBody Map<String, String> request) {
		String acct = request.get("acct");
		boolean exists = pService.findOne(acct) != null;
		Map<String, Object> response = new HashMap<>();
		response.put("success", !exists);// 如果帳號存在，success 為 false；如果帳號不存在，success 為 true
		return ResponseEntity.ok(response);
	}

	// 獲取所有會員資料
	@ResponseBody
	@GetMapping("/profile/admin/api/getall")
	public List<ProfilesDTO> getAllProfiles(HttpServletRequest request) {
		List<ProfilesBean> profiles = pService.findAll();

		// 讀取 application.properties 中的存儲路徑
		String url = storagePrefix + "profShot/";
		// 動態獲取當前服務的 base URL
		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString()
				+ "/img/profShot/";
		// 將 ProfilesBean 轉換為 ProfilesDTO
		List<ProfilesDTO> profileDTOs = profiles.stream().map(profile -> {
			ProfilesDTO dto = new ProfilesDTO();
			dto.setProfileId(profile.getProfileId());
			dto.setAcct(profile.getAcct());
			dto.setUserName(profile.getUserName());
			dto.setEmail(profile.getEmail());
			dto.setPhone(profile.getPhone());
			dto.setAddress1(profile.getAddress1());
			dto.setGender(profile.getGender());
			dto.setBirth(profile.getBirth());
			// 設置頭像 URL
			String fileName = profile.getAcct() + ".jpg"; // 照片名稱
			File photoFile = new File(url + fileName); // 磁碟上的完整路徑
			if (photoFile.exists()) {
				dto.setShot(baseUrl + fileName);
			} else {
				dto.setShot(baseUrl + "default.jpg"); // 預設照片
			}

			dto.setRoleName(profile.getRolesBean().getRoleName()); // 設置角色名稱為字符串
			dto.setAcctStatus(profile.getAcctStatus());

			return dto;
		}).collect(Collectors.toList());

		return profileDTOs;
	}

	/*
	 * for (ProfilesBean profile : profiles) { String fileName = profile.getAcct() +
	 * ".jpg"; // 照片名稱 File photoFile = new File(url + fileName); // 磁碟上的完整路徑
	 * 
	 * // 如果照片存在，設置完整路徑；否則設置預設頭像 if (photoFile.exists()) { profile.setShot(baseUrl +
	 * fileName); } else { profile.setShot(baseUrl + "default.jpg"); // 預設照片 } }
	 * return profiles; }
	 */
	// 查詢單筆會員資料
	@ResponseBody
	@GetMapping("/profile/member/api/getone/{acct}")
	public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String acct) {
		Map<String, Object> response = new HashMap<>();

		try {
			ProfilesBean pBean = pService.findOne(acct); // 從資料庫獲取會員資料
			if (pBean != null) {
				response.put("success", true);
				response.put("data", pBean);
				return ResponseEntity.ok(response);
			} else {
				response.put("success", false);
				response.put("message", "找不到會員資料");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "查詢資料時發生錯誤: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 刪除會員資料
	@ResponseBody
	@DeleteMapping("/profile/admin/api/delete/{acct}")
	public ResponseEntity<String> deleteProfile(@PathVariable String acct) {
		pService.deleteByAcct(acct); // 根據帳號刪除會員
		return ResponseEntity.ok("會員帳號" + acct + "已刪除");
	}

	// 修改會員資料
//	@PreAuthorize("#acct == authentication.name") //只允許本人修改自己的資料
//	@PutMapping(value = "/profile/member/api/update/{acct}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	@PutMapping(value = "/profile/member/api/update/{acct}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> updateProfile(@PathVariable String acct,
//	        @RequestPart(value = "pwd", required = false) String pwd,
			@RequestPart("profile") ProfilesBean pBean, // JSON 數據
			@RequestPart(value = "shot", required = false) MultipartFile shot) { // 頭像文件

		Map<String, Object> response = new HashMap<>();
		try {
			// 確保 `ProfilesBean` 正確序列化和映射
			if (pBean == null) {
				response.put("success", false);
				response.put("message", "Profile 資料為空");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			// 取得現有的會員資料
			ProfilesBean existingProf = pService.findOne(acct);
			if (existingProf == null) {
				response.put("success", false);
				response.put("message", "會員不存在");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// **保留舊值，只有有新值時才更新**
			existingProf.setUserName(pBean.getUserName() != null ? pBean.getUserName() : existingProf.getUserName());
//			existingProf.setEmail(pBean.getEmail());
			existingProf.setPhone(pBean.getPhone() != null ? pBean.getPhone() : existingProf.getPhone());
			existingProf.setAddress1(pBean.getAddress1() != null ? pBean.getAddress1() : existingProf.getAddress1());
//			existingProf.setRolesBean(pBean.getRolesBean() != null ? pBean.getRolesBean() : existingProf.getRolesBean());
//			existingProf.setAcctStatus(pBean.getAcctStatus() != null ? pBean.getAcctStatus() : existingProf.getAcctStatus());

//			// 如果提供密碼，則更新密碼
//			if (pBean.getPwd() != null && !pBean.getPwd().isEmpty()) {
//				existingProf.setPwd(pBean.getPwd()); // 請根據需要加密密碼
//			}
			
			// **避免 email 設為 null，防止 UNIQUE KEY 錯誤**
	        if (pBean.getEmail() != null && !pBean.getEmail().isEmpty()) {
	            existingProf.setEmail(pBean.getEmail());
	        }

			// 處理頭像更新
			if (shot != null && !shot.isEmpty()) {
				String fileName = existingProf.getAcct() + ".jpg";
				File uploadDir = new File(storagePrefix + "/profShot");
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}
				File uploadFile = new File(uploadDir, fileName);
				shot.transferTo(uploadFile);
				existingProf.setShot("img/profShot/" + fileName);
			}
			// 如果沒有新頭像，則保留原本的頭像
			else {
				existingProf.setShot(existingProf.getShot());
			}

			// **確保 `acctStatus` 不會被重設**
	        if (pBean.getAcctStatus() != null) {
	            existingProf.setAcctStatus(pBean.getAcctStatus());
	        }

	        // **確保 `rolesBean` 不會變成 `null`**
	        if (pBean.getRolesBean() != null && pBean.getRolesBean().getRoleName() != null) {
	            existingProf.setRolesBean(pBean.getRolesBean());
	        }
	        
			ProfilesBean savedBean = pService.update(existingProf);

			response.put("success", true);
			response.put("message", "會員資料修改成功");
			response.put("data", savedBean);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "修改資料時發生錯誤: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	/*
	 * @PutMapping(value = "/api/update/{acct}") public ResponseEntity<Map<String,
	 * Object>> updateProfile(
	 * 
	 * @PathVariable String acct,
	 * 
	 * @RequestBody(required = false) ProfilesBean pBean, // JSON 數據
	 * 
	 * @RequestParam(value = "shot", required = false) MultipartFile shot) { // 頭像文件
	 * 
	 * Map<String, Object> response = new HashMap<>(); try { if (pBean != null) { //
	 * 處理資料更新 return handleProfileUpdate(acct, pBean, response); } else if (shot !=
	 * null && !shot.isEmpty()) { // 處理頭像更新 return handleAvatarUpdate(acct, shot,
	 * response); } else { response.put("success", false); response.put("message",
	 * "無效的請求"); return
	 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); } } catch
	 * (Exception e) { response.put("success", false); response.put("message",
	 * "修改資料時發生錯誤: " + e.getMessage()); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); } }
	 * 
	 * private ResponseEntity<Map<String, Object>> handleProfileUpdate(String acct,
	 * ProfilesBean pBean, Map<String, Object> response) { try { ProfilesBean
	 * existingProf = pService.findOne(acct); if (existingProf == null) {
	 * response.put("success", false); response.put("message", "會員不存在"); return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); }
	 * 
	 * // 確保 pBean 內的值不為 null，避免 Hibernate 錯誤 if (pBean.getUserName() != null)
	 * existingProf.setUserName(pBean.getUserName()); if (pBean.getEmail() != null)
	 * existingProf.setEmail(pBean.getEmail()); if (pBean.getPhone() != null)
	 * existingProf.setPhone(pBean.getPhone()); if (pBean.getAddress1() != null)
	 * existingProf.setAddress1(pBean.getAddress1()); if (pBean.getRoleName() !=
	 * null) existingProf.setRoleName(pBean.getRoleName()); if
	 * (pBean.getAcctStatus() != null)
	 * existingProf.setAcctStatus(pBean.getAcctStatus());
	 * 
	 * // // 確保密碼加密處理 // if (pBean.getPwd() != null && !pBean.getPwd().isEmpty()) {
	 * // String encryptedPwd = passwordEncoder.encode(pBean.getPwd()); // 假設使用
	 * BCrypt 加密 // existingProf.setPwd(encryptedPwd); // }
	 * 
	 * ProfilesBean savedBean = pService.update(existingProf);
	 * response.put("success", true); response.put("message", "會員資料修改成功");
	 * response.put("data", savedBean); return ResponseEntity.ok(response); } catch
	 * (Exception e) { response.put("success", false); response.put("message",
	 * "修改資料時發生錯誤: " + e.getMessage()); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); } }
	 * 
	 * private ResponseEntity<Map<String, Object>> handleAvatarUpdate(String acct,
	 * MultipartFile shot, Map<String, Object> response) { ProfilesBean existingProf
	 * = pService.findOne(acct); if (existingProf == null) { response.put("success",
	 * false); response.put("message", "會員不存在"); return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); }
	 * 
	 * // 處理頭像更新 String fileName = existingProf.getAcct() + ".jpg"; File uploadDir =
	 * new File(storagePrefix + "/profShot"); if (!uploadDir.exists()) {
	 * uploadDir.mkdirs(); } File uploadFile = new File(uploadDir, fileName); try {
	 * shot.transferTo(uploadFile); } catch (IOException e) {
	 * response.put("success", false); response.put("message", "頭像上傳失敗: " +
	 * e.getMessage()); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); }
	 * 
	 * existingProf.setShot("/img/profShot/" + fileName); ProfilesBean savedBean =
	 * pService.update(existingProf); response.put("success", true);
	 * response.put("message", "頭像更新成功"); response.put("data", savedBean); return
	 * ResponseEntity.ok(response); }
	 */
	// http://localhost:8080/profile/querybypage/1
	@PostMapping("querybypage/{pageNo}")
	@ResponseBody
	public HashMap<String, Object> processQueryAllByPageAction(@PathVariable Integer pageNo) {
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<ProfilesBean> page = pService.findAllByPage(pageable);

		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("totalPages", page.getTotalPages());
		maps.put("totalElements", page.getTotalElements());
		maps.put("content", page.getContent());

		return maps;
	}

}
