package team5.order.model;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private int productID;
    private int orderDetailQuantity;
    private double orderDetailProductPrice;

    public OrderDetailDTO(OrderDetailBean orderDetail) {
        this.productID = orderDetail.getId().getProductID();
        this.orderDetailQuantity = orderDetail.getOrderDetailQuantity();
        this.orderDetailProductPrice = orderDetail.getOrderDetailProductPrice();
    }
}

