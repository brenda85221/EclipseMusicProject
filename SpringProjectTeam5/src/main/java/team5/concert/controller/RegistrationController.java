package team5.concert.controller;



import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import team5.concert.model.RegistrationService;

import team5.concert.model.bean.Registration;


//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;
  
    // 串綠界
    @PostMapping("/ecpayCheckout")
    public ResponseEntity<Map<String, String>> ecpayCheckout(@RequestBody Map<String, Object> requestData) {
        try {
            Integer registrationId = (Integer) requestData.get("registrationId");
            String paymentMethod = (String) requestData.get("paymentMethod");
            String totalAmount = (String) requestData.get("totalAmount");
          
            // 檢查請求參數
            if (registrationId == null || paymentMethod == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required parameters"));
            }

            // 調用服務生成支付表單
            String paymentForm = registrationService.createPaymentOrder(registrationId, paymentMethod,totalAmount);

            // 返回生成的付款表單
            return ResponseEntity.ok(Map.of("form", paymentForm));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment processing failed: " + e.getMessage()));
        }
    }

  
    
    // 新增報名
    @PostMapping
    public ResponseEntity<Registration> createRegistration(@RequestBody Registration registration) {
        try {
            Registration createdRegistration = registrationService.createRegistration(registration);
            return new ResponseEntity<>(createdRegistration, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // 返回 400
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);  // 返回 409 (conflict)
        }
    }  
    

    // 查詢所有報名
    @GetMapping
    public ResponseEntity<List<Registration>> getAllRegistrations() {
        List<Registration> registrations = registrationService.getAll();
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }
    

    /**
     * 假刪除某個報名記錄
     * @param registrationId 要假刪除的報名記錄 ID
     * @return 假刪除成功的報名記錄或錯誤信息
     */
    @DeleteMapping("/{registrationId}")
    public ResponseEntity<?> softDeleteRegistration(@PathVariable Integer registrationId) {
        Optional<Registration> registrationOpt = registrationService.softDeleteRegistration(registrationId);

        if (registrationOpt.isPresent()) {
            return ResponseEntity.ok("報名記錄已成功刪除");
        } else {
            return ResponseEntity.status(404).body("找不到該報名記錄，無法執行刪除");
        }
    }
    

  
    /**
     * 更新報名記錄
     * @param registrationId 要更新的報名記錄 ID
     * @param registration 更新後的報名資料
     * @return 更新結果
     */
    @PutMapping("/{registrationId}")
    public ResponseEntity<?> updateRegistration(@PathVariable Integer registrationId, @RequestBody Registration registration) {
        // 呼叫 service 層的更新方法
        Optional<Registration> updatedRegistrationOpt = registrationService.updateRegistration(registrationId, registration);

        if (updatedRegistrationOpt.isPresent()) {
            // 更新成功，返回更新後的報名記錄
            return ResponseEntity.ok(updatedRegistrationOpt.get());
        } else {
            // 更新失敗，找不到對應的報名記錄
            return ResponseEntity.status(404).body("找不到該報名記錄，無法執行更新");
        }
    }

   

    
    // 根據會員 ID 查詢報名
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<Registration>> getRegistrationsByProfileId(@PathVariable Integer profileId) {
        List<Registration> registrations = registrationService.getRegistrationsByProfileId(profileId);
        return ResponseEntity.ok(registrations);
    }

 
    
    // 透過 concertId 查詢報名資料
    @GetMapping("/concerts/{concertId}")
    public ResponseEntity<List<Registration>> getRegistrationsByConcertId(@PathVariable Integer concertId) {
        List<Registration> registrations = registrationService.getRegistrationsByConcertId(concertId);
        if (registrations.isEmpty()) {
            return ResponseEntity.noContent().build(); // 若沒有資料，回傳 204 No Content
        }
        return ResponseEntity.ok(registrations); // 正常回傳資料並帶上 200 OK 狀態
    }
    

    
}