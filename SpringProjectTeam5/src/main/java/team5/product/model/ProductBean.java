package team5.product.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="product")
public class ProductBean {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int productID;
	private String productName;
	private String productDescription;
	private double price;
	private int stock;
	private int categoryId;
	private String imageUrl;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime updatedAt =  LocalDateTime.now();
	private int isActive=0;
	
	

	
}
