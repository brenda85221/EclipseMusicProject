package team5.order.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name="orders")
public class OrderBean {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderID;
    private String acct;
    private LocalDateTime orderDate =  LocalDateTime.now();
    private String orderState ="未完成";
    private double totalAmount;
    private double orderDiscountValue;
    private String paymentStatus="未付款";
    private String couponID;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailBean> orderDetails;


}
