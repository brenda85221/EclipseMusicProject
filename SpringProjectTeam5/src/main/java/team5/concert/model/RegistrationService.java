package team5.concert.model;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import team5.concert.model.bean.Concert;
import team5.concert.model.bean.Registration;
import team5.profile.model.ProfilesRepository;
import team5.profile.model.bean.ProfilesBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistrationService {
	
		@Autowired
	    private final RegistrationRepository registrationRepository;

	    @Autowired
	    private final ConcertRepository concertRepository;
	    
	    @Autowired
	    private final ProfilesRepository pDao;

	    public RegistrationService(RegistrationRepository registrationRepository, ConcertRepository concertRepository,ProfilesRepository pDao) {
	        this.registrationRepository = registrationRepository;
	        this.concertRepository = concertRepository;
	        this.pDao = pDao;
	    }

	 // 查詢所有
	    public List<Registration> getAll() {
	        return registrationRepository.findAll();
	    }
	    
	    
	    // 串綠界金流
	    public String createPaymentOrder(Integer registrationId, String paymentMethod,String totalAmount) {
	        AllInOne all = new AllInOne("");
	        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
	        AioCheckOutALL obj = new AioCheckOutALL();
	        obj.setMerchantID("3002607"); // 商家代號
	        obj.setMerchantTradeNo(uuId); // 唯一交易編號
	        obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())); // 當前時間
	        obj.setPaymentType("aio"); // 付款類型
	        obj.setTotalAmount(totalAmount); 
	        obj.setTradeDesc("Concert Registration Payment"); // 交易描述
	        obj.setItemName("音樂活動票券"); // 商品名稱

	        // 設定回傳 URL
	        obj.setReturnURL("http://localhost:8080/registrations/return"); // 用於接收付款結果的回調地址
	        obj.setClientBackURL("http://localhost:5174/#/registration/history"); // 用戶支付完成後跳轉的頁面
//	        obj.setOrderResultURL("https://your-domain.com/ecpay/result"); // 顯示交易結果的頁面

	        // 根據用戶選擇的付款方式設定
	        if ("信用卡".equals(paymentMethod)) {
	            obj.setChoosePayment("Credit");
	        } else if ("ATM轉帳".equals(paymentMethod)) {
	            obj.setChoosePayment("ATM");
	        } else {
	            throw new IllegalArgumentException("不支持的付款方式: " + paymentMethod);
	        }

	        obj.setEncryptType("1"); // 加密方式
	        obj.setNeedExtraPaidInfo("N"); // 是否需要額外的付款資訊

	        // 生成付款 HTML 表單
	        return all.aioCheckOut(obj, null);
	    }
	    
	    
	    //新增報名
	    public Registration createRegistration(Registration registration) {
	        // 1. 查詢 Concert 實體，這裡使用 concertId 設置 concert 關聯
	        Concert concert = concertRepository.findById(registration.getConcert().getConcertId())
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "所選的活動不存在"));

	        // 2. 只在 profile 不為 null 的情況下查詢 Profile 實體
	        if (registration.getProfile() != null) {
	            ProfilesBean profile = pDao.findById(registration.getProfile().getProfileId())
	                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "所選的會員不存在"));
	            registration.setProfile(profile);  // 設置 Profile 關聯
	        } else {
	            registration.setProfile(null);  // 若 Profile 為 null，設為 null
	        }

	        // 3. 假設這裡有防止重複報名的邏輯，檢查用戶是否已報名過此活動
	        if (isAlreadyRegistered(registration)) {
	            throw new ResponseStatusException(HttpStatus.CONFLICT, "您已經報名過此活動");
	        }

	        // 4. 檢查報名截止日期
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        if (concert.getRegistrationDeadline() != null && currentDateTime.isAfter(concert.getRegistrationDeadline())) {
	            throw new ResponseStatusException(HttpStatus.CONFLICT, "報名已截止，無法再進行報名");
	        }

	        // 5. 計算已報名的總票數，檢查是否超過最大參與人數
	        Integer currentRegisteredTicketCount = registrationRepository.countTicketByConcert(concert); // 查詢該活動的總票數

	        // 如果 currentRegisteredTicketCount 為 null，設置為 0
	        if (currentRegisteredTicketCount == null) {
	            currentRegisteredTicketCount = 0;
	        }

	        if (currentRegisteredTicketCount + registration.getTicketCount() > concert.getMaxRegistrants()) {
	            throw new ResponseStatusException(HttpStatus.CONFLICT, "報名票數已達上限，無法完成報名");
	        }

	        // 6. 設置 Concert 和 Profile 關聯
	        registration.setConcert(concert);

	        // 7. 設置報名時間
	        registration.setRegistrationTime(LocalDateTime.now());
	        registration.setIsDeleted(false);
	        registration.setRegistStatus("已報名");
	        registration.setPaymentMethod(registration.getPaymentMethod());

	        // 8. 儲存報名資料
	        return registrationRepository.save(registration);
	    }



	    // 假設有這樣一個方法來檢查是否已經報名
	    private boolean isAlreadyRegistered(Registration registration) {
	        return registrationRepository.existsByConcertAndProfile(registration.getConcert(), registration.getProfile());
	    }

	    // 根據 ID 查詢報名
	    public Optional<Registration> getRegistrationById(Integer registrationId) {
	        return registrationRepository.findById(registrationId);
	    }
	    
	
	    /**
	     * 更新報名記錄
	     * @param registrationId 報名記錄 ID
	     * @param updatedRegistration 更新後的報名資料
	     * @return 更新後的報名記錄
	     */
	    @Transactional
	    public Optional<Registration> updateRegistration(Integer registrationId, Registration updatedRegistration) {
	        // 查詢現有的報名記錄
	        Optional<Registration> existingRegistrationOpt = registrationRepository.findByRegistrationId(registrationId);

	        if (existingRegistrationOpt.isPresent()) {
	            Registration existingRegistration = existingRegistrationOpt.get();

	            // 假設可以通過 registration 來取得 concert（活動資訊）
	            Concert concert = existingRegistration.getConcert();

	            // 計算已報名的總票數
	            Integer currentRegisteredTicketCount = registrationRepository.countTicketByConcert(concert); // 查詢該活動的總票數

	            // 如果 currentRegisteredTicketCount 為 null，設置為 0
	            if (currentRegisteredTicketCount == null) {
	                currentRegisteredTicketCount = 0;
	            }

	            // 更新後的報名票數
	            Integer newTicketCount = updatedRegistration.getTicketCount();
	            
	            // 檢查報名票數是否超過最大人數
	            if (currentRegisteredTicketCount + newTicketCount - existingRegistration.getTicketCount() > concert.getMaxRegistrants()) {
	                throw new IllegalStateException("報名票數已達上限，無法完成更新");
	            }

	            // 更新報名記錄的欄位
	            existingRegistration.setRegistStatus(updatedRegistration.getRegistStatus());
	            existingRegistration.setPaymentMethod(updatedRegistration.getPaymentMethod());
	            existingRegistration.setTicketCount(newTicketCount);
	            existingRegistration.setTicketPrice(updatedRegistration.getTicketPrice());
	            existingRegistration.setTotalAmount(updatedRegistration.getTotalAmount());

	            // 如果有更新 isDeleted 欄位，則保持更新
	            if (updatedRegistration.getIsDeleted() != null) {
	                existingRegistration.setIsDeleted(updatedRegistration.getIsDeleted());
	            }

	            // 保存更新的報名記錄
	            return Optional.of(registrationRepository.save(existingRegistration));
	        }

	        // 如果找不到相應的報名記錄，返回 Optional.empty()
	        return Optional.empty();
	    }

	
	    
	    /**
	     * 假刪除報名記錄
	     * @param registrationId 要假刪除的報名記錄 ID
	     * @return 假刪除後的報名記錄，若找不到則返回空
	     */
	    @Transactional
	    public Optional<Registration> softDeleteRegistration(Integer registrationId) {
	        Optional<Registration> registrationOpt = registrationRepository.findByRegistrationId(registrationId);

	        if (registrationOpt.isPresent()) {
	            Registration registration = registrationOpt.get();
	            registration.setIsDeleted(true);  // 標註為已刪除
	            registrationRepository.save(registration);  // 保存更新後的記錄
	            return Optional.of(registration);
	        } else {
	            return Optional.empty();  // 若無此報名記錄，返回空
	        }
	    }



	    // 查詢某活動的所有報名
	    public List<Registration> getRegistrationsByConcertId(Integer concertId) {
	        return registrationRepository.findByConcert_ConcertId(concertId);
	    }

	    
	    
	    // 查詢特定會員的所有報名
	    public List<Registration> getRegistrationsByProfileId(Integer profileId) {
	        return registrationRepository.findByProfile_profileId(profileId);
	    }

		public void save(Registration registration) {
			// TODO Auto-generated method stub
			
		}


	
		
		 
	    

}
