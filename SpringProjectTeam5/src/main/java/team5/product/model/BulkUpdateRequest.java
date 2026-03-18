package team5.product.model;

import java.util.List;

import lombok.Data;

@Data
public class BulkUpdateRequest {
	private List<Integer> productIDs; // 產品 ID 清單
    private int isActive; // 0 = 上架, 1 = 下架
    
   
}
