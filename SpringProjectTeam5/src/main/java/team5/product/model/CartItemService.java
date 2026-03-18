package team5.product.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartItemService {
	
	@Autowired
    private CartItemRepository cartItemRepository;
    
	public void addToCart(String acct, int productId, int quantity) {
	    CartItemId cartItemId = new CartItemId(acct, productId);
	    System.out.println(quantity);

	    // 查找是否已有這個商品在購物車中
	    CartItemBean cartItem = cartItemRepository.findById(cartItemId)
	        .orElseGet(() -> {
	            // 如果沒有，創建一個新的購物車項目並保存
	            CartItemBean newCartItem = new CartItemBean();
	            newCartItem.setId(cartItemId);
	            newCartItem.setQuantity(quantity); // 設置初始數量為當前選擇的數量
	            cartItemRepository.save(newCartItem);  // 創建新商品並保存
	            System.out.println("創建新的購物車項目，數量：" + quantity);
	            return null;  // 返回 null，表示已經創建並保存，退出方法
	        });

	    // 如果找到了已存在的購物車項目，則更新數量
	    if (cartItem != null) {
	        System.out.println("原本的數量: " + cartItem.getQuantity());
	        System.out.println("新加的數量: " + quantity);
	        cartItem.setQuantity(cartItem.getQuantity() + quantity);  // 增加數量
	        cartItemRepository.save(cartItem);  // 更新數量
	    }
	}

    
    public List<CartItemBean> getCartItems(String acct) {
        return cartItemRepository.findByIdAcct(acct);
    }
    
    public void removeFromCart(String acct, int productId) {
        cartItemRepository.deleteById(new CartItemId(acct, productId));
    }
    
    // 更新購物車
    public void updateCart(String acct, List<CartItemBean> cartItems) {
        // 遍歷所有購物車項目
        for (CartItemBean cartItem : cartItems) {
            // 構建 CartItemId 作為複合主鍵 (假設你使用複合主鍵)
            CartItemId cartItemId = new CartItemId(acct, cartItem.getProduct().getProductID());

            // 查找是否已存在這個商品
            CartItemBean existingCartItem = cartItemRepository.findById(cartItemId).orElse(null);

            if (existingCartItem != null) {
                // 如果商品已存在，更新數量
                existingCartItem.setQuantity(cartItem.getQuantity());
                cartItemRepository.save(existingCartItem);  // 保存更新後的購物車項目
            } else {
                // 如果商品不存在，創建新購物車項目
                cartItem.setId(cartItemId);  // 設置複合主鍵
                cartItemRepository.save(cartItem);  // 保存新創建的購物車項目
            }
        }
    }
    
    //綠介科技金流串流
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
        obj.setItemName("Inspire購物商城"); // 商品名稱

        // 設定回傳 URL
        obj.setReturnURL("http://localhost:8080/order/payReturn"); // 用於接收付款結果的回調地址
        obj.setClientBackURL("https://your-domain.com/confirmation"); // 用戶支付完成後跳轉的頁面
//        obj.setOrderResultURL("https://your-domain.com/ecpay/result"); // 顯示交易結果的頁面（可選）

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
}
