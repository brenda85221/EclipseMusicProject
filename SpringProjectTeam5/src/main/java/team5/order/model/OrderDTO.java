package team5.order.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {
    private int orderID;
    private String acct;
    private LocalDateTime orderDate;
    private String orderState;
    private double totalAmount;
    private double orderDiscountValue;
    private String paymentStatus;
    private String couponID;
    private List<OrderDetailDTO> orderDetails;  // 假設 OrderDetailDTO 用來顯示 OrderDetailBean 的資料

    // 假設有一個 OrderBean 的建構子
    public OrderDTO(OrderBean orderBean) {
        this.orderID = orderBean.getOrderID();
        this.acct = orderBean.getAcct();
        this.orderDate = orderBean.getOrderDate();
        this.orderState = orderBean.getOrderState();
        this.totalAmount = orderBean.getTotalAmount();
        this.orderDiscountValue = orderBean.getOrderDiscountValue();
        this.paymentStatus = orderBean.getPaymentStatus();
        this.couponID = orderBean.getCouponID();

        // 假設 OrderDetailDTO 可以從 OrderDetailBean 初始化
        this.orderDetails = orderBean.getOrderDetails().stream()
                                      .map(OrderDetailDTO::new)  // 將 OrderDetailBean 轉換成 OrderDetailDTO
                                      .collect(Collectors.toList());
    }

    
}









