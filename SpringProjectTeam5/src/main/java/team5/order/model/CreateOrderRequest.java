package team5.order.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String acct;
    private List<OrderDetailRequest> orderDetails;
    private double totalAmount;

    // getters and setters
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailRequest {
        private int productID;
        private int quantity;
        private double price;

        // getters and setters
    }
}
