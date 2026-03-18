package team5.order.model;

import org.springframework.stereotype.Component;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team5.product.model.ProductBean;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name="orderDetails")
public class OrderDetailBean {
	
	@EmbeddedId
    private OrderDetailId id; 
      
    private int orderDetailQuantity;
    private double orderDetailProductPrice; 

    @ManyToOne
//    @JsonBackReference
//    @JsonIgnore
    @MapsId("orderID") 
    @JoinColumn(name = "orderID")
    private OrderBean order;
    
    @ManyToOne
    @MapsId("productID") 
    @JoinColumn(name = "productID")
    private ProductBean product;
    
//    public OrderDetailBean(int orderID, int productID, int quantity, double price) {
//        this.id = new OrderDetailId(orderID, productID);
//        this.orderDetailQuantity = quantity;
//        this.orderDetailProductPrice = price;
//    }
    
    public OrderDetailBean(OrderBean order, ProductBean product, int quantity, double price) {
        this.id = new OrderDetailId(order.getOrderID(), product.getProductID()); // ✅ orderID 來自已存入的 OrderBean
        this.order = order;
        this.product = product;
        this.orderDetailQuantity = quantity;
        this.orderDetailProductPrice = price;
    }


}
