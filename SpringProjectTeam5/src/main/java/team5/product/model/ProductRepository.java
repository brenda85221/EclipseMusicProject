package team5.product.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductBean, Integer> {
	// 查詢 isActive = 0 或 1 的產品
    List<ProductBean> findByIsActive(int isActive);
    
    //圖表用，一次查詢多個商品名稱
    List<ProductBean> findByProductIDIn(List<Long> productIDs);
}
