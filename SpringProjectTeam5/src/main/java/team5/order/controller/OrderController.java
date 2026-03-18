package team5.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import team5.order.model.CreateOrderRequest;
import team5.order.model.OrderBean;
import team5.order.model.OrderDTO;
import team5.order.model.OrderDetailRepository;
import team5.order.model.OrderRepository;
import team5.order.model.OrderService;
import team5.product.model.ProductBean;

//@CrossOrigin(origins = "*")
@RestController
public class OrderController {
	//test
    @Autowired
    private OrderService oService;
    
    @Autowired
    private OrderRepository orderRepository;

//    @GetMapping("/order/selectById/{orderId}")
//    public OrderDTO processQueryByIdAction(@PathVariable int orderId) {
//        OrderBean order = oService.getOrderById(orderId);
//        return order != null ? new OrderDTO(order) : null;
//    }
    
    @GetMapping("/order/selectById/{orderId}")
    public ResponseEntity<OrderDTO> processQueryByIdAction(@PathVariable int orderId) {
        OrderBean order = oService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(new OrderDTO(order)); // 返回 200 OK 和資料
        } else {
            return ResponseEntity.notFound().build(); // 返回 404 Not Found
        }
    }
    
    @PostMapping("/order/create")
    public ResponseEntity<OrderBean> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            // 只傳遞 acct，因為 createOrder 方法只需要這個參數
            OrderBean order = oService.createOrder(request.getAcct());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace();  // 打印異常信息
            return ResponseEntity.badRequest().body(null);
        }
    }
 
    @GetMapping("/order/getAllOrder")
    public List<OrderDTO> processGetAllOrder() {
        return oService.getAllOrder();
    }

    @DeleteMapping("/order/delete/{orderId}")
    public String processDeleteAction(@PathVariable int orderId) {
        OrderBean order = oService.getOrderById(orderId);
        if (order != null) {
            oService.deleteOrder(orderId);
            return "Delete OK";
        }
        return "Delete not OK";
    }
    
 // 更新訂單
    @PutMapping("/order/update/{orderID}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable int orderID, @RequestBody OrderDTO updatedOrder) {
        OrderDTO order = oService.updateOrder(orderID, updatedOrder);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }
    
    //前台用
    @GetMapping("/order/selectByAcct")
	public List<OrderDTO> processQueryByAcctAction() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String acct = (String) authentication.getCredentials(); 
	    System.out.println("找不到帳號?"+acct);
	    return oService.seleteByAcct(acct);
//	    List<OrderDTO> resultBean = 
//		return resultBean != null ? new OrderDTO(resultBean) : null;
//		return resultBean;
	}
    
    //處理付款後的訂單付款狀態更新
    @PostMapping("/order/payReturn")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam Integer orderId) {
    	System.out.println("gooooood");
        // 根據訂單編號查找訂單
    	 OrderBean order = oService.getOrderById(orderId);
        
        // 如果找不到訂單，回傳錯誤訊息
        if (order == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("訂單不存在");
        }

        // 更新支付狀態為已付款
        order.setPaymentStatus("已付款");
        orderRepository.save(order); // 保存更新後的訂單
        
        // 返回成功訊息
        return ResponseEntity.ok("訂單支付成功，已更新為已付款狀態");
    }
}
