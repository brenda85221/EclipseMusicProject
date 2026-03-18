package team5.product.model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository pRepos;
	
	@Autowired
    private SupabaseStorageService storageService;
		
	public void deleteById(Integer id) {
		pRepos.deleteById(id);
	}
	
	public ProductBean selectById(Integer id) {
	    return pRepos.findById(id).orElse(null);
	}
	
	public List<ProductBean> getAllProduct() {
        return pRepos.findAll();
    }
	
    // 查詢下架的所有產品
    public List<ProductBean> getAllUnavailableProducts() {
        return pRepos.findByIsActive(0);  // isActive = 0 表示下架
    }
    
    // 查詢上架的所有產品
    public List<ProductBean> getAllAvailableProducts() {
    	return pRepos.findByIsActive(1);  // isActive = 1 表示上架
    }

    // 查詢已假刪除的所有產品
    public List<ProductBean> getAllDeletedProducts() {
        return pRepos.findByIsActive(2);  // isActive = 2 表示假刪除
    }
	
//	private static final String UPLOAD_DIR = "uploads/";  // 設定圖片儲存的目錄
	
    // 插入商品並處理圖片
    public ProductBean insert(ProductBean pBean, MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            String imageUrl = storageService.uploadImage(photo, "product-images");
            pBean.setImageUrl(imageUrl);
        }
        return pRepos.save(pBean);
    }
    
    public ProductBean updateWithoutPhoto(ProductBean pBean) {	
		return pRepos.save(pBean);
	}
    
    public ProductBean update(ProductBean pBean, MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            String imageUrl = storageService.uploadImage(photo, "product-images");
            pBean.setImageUrl(imageUrl);
        }
        // 若沒有新圖，保持傳入的pBean裡原有imageUrl不變
        return pRepos.save(pBean);
    }
    
    public void bulkUpdateIsActive(List<Integer> productIDs, int isActive) {
        if (productIDs == null || productIDs.isEmpty()) {
            return;
        }
        List<ProductBean> products = pRepos.findAllById(productIDs);
        for (ProductBean product : products) {
            product.setIsActive(isActive);
        }
        pRepos.saveAll(products);
    }
  
    public boolean updateProductIsActive(Integer productID) {
        Optional<ProductBean> existingProduct = pRepos.findById(productID);
        System.out.println(existingProduct);
        if (!existingProduct.isPresent()) {
            return false;
        }

        ProductBean product = existingProduct.get();
        product.setIsActive(2); // 統一設定 isActive = 2 (刪除)
//        System.out.println(product);
        pRepos.save(product); // JPA 自動更新
        return true;
    }

  //圖表用，一次查詢多個商品名稱
    public List<ProductBean> findProductsByIds(List<Long> productIDs) {
        return pRepos.findByProductIDIn(productIDs);
    }

	
}
