package team5.product.model;

import java.util.List;

import lombok.Data;

@Data
public class CartRequestDTO {
	 private String acct;
    private List<CartItemBean> cartItems;
}
