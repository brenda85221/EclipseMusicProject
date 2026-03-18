package team5.product.model;


import org.springframework.stereotype.Component;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name="cartItem")
public class CartItemBean {
	
	@EmbeddedId
    private CartItemId id;

	private int quantity;
    
    @ManyToOne
//    @JoinColumn(name = "productID")
    @JoinColumn(name = "productID", insertable = false, updatable = false)
    private ProductBean product;
    

	
}
