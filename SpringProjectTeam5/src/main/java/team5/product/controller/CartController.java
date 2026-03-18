package team5.product.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import team5.product.model.CartItemBean;
import team5.product.model.CartItemService;
import team5.product.model.CartRequestDTO;

//@CrossOrigin(origins = "*")
@RestController
public class CartController {
	@Autowired
    private CartItemService cartService;
    
    @PostMapping("/product/cartadd")
    public ResponseEntity<String> addToCart(@RequestParam String acct, @RequestParam int productID, @RequestParam int quantity) {
    	cartService.addToCart(acct, productID, quantity);
        return ResponseEntity.ok("Product added to cart");
    }
    
    @PostMapping("/product/cartitems")
    public ResponseEntity<List<CartItemBean>> getCartItems(@RequestBody Map<String, String> request) {
        String acct = request.get("acct");  // 從請求體中獲取帳號
        return ResponseEntity.ok(cartService.getCartItems(acct));
    }

//    @DeleteMapping("/product/cartremove")
//    public ResponseEntity<String> removeFromCart(@RequestParam String acct, @RequestParam int productId) {
//        try {
//            cartService.removeFromCart(acct, productId);
//            return ResponseEntity.ok("Delete OK");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing product");
//        }
//    }
    
    @DeleteMapping("/product/cartremove/{productId}")
    public ResponseEntity<String> removeFromCart( @PathVariable int productId) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String acct = (String) authentication.getCredentials(); 
	    System.out.println("productID"+productId);
        try {
            cartService.removeFromCart(acct, productId);
            return ResponseEntity.ok("Delete OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing product");
        }
    }
    
    @PostMapping("/product/updateCart")
    public ResponseEntity<String> updateCart(@RequestBody CartRequestDTO cartRequestDTO) {
        try {
            String acct = cartRequestDTO.getAcct(); // 獲取帳號
            List<CartItemBean> cartItems = cartRequestDTO.getCartItems(); // 獲取購物車項目
            
            System.out.println(cartItems);
            cartService.updateCart(acct, cartItems); // 傳遞帳號和購物車項目到服務層
            return ResponseEntity.ok("購物車已成功更新！");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新購物車時出錯！");
        }
    }
    
    @PostMapping("/product/ecpayCheckout")
    public ResponseEntity<Map<String, String>> ecpayCheckout(@RequestBody Map<String, Object> requestData) {
        try {
            Integer registrationId = Integer.parseInt(requestData.get("registrationId").toString());  // ✅ 修正
            String paymentMethod = (String) requestData.get("paymentMethod");
            String totalAmount = requestData.get("totalAmount").toString();  // 確保 totalAmount 也是 String

            if (registrationId == null || paymentMethod == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required parameters"));
            }

            String paymentForm = cartService.createPaymentOrder(registrationId, paymentMethod, totalAmount);
            return ResponseEntity.ok(Map.of("form", paymentForm));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment processing failed: " + e.getMessage()));
        }
    }

}
