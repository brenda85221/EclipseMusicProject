package team5.product.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import team5.product.model.BulkUpdateRequest;
import team5.product.model.ProductBean;
import team5.product.model.ProductService;
import team5.profile.model.bean.ProfilesBean;
import team5.profile.model.service.ProfilesService;

//@CrossOrigin(origins = "http://localhost:")
//@CrossOrigin(origins = "*")
@RestController
public class ProductController {
	
	@Autowired
	private ProductService pService;
	
	@Autowired
	private ProfilesService pfService;
	
	//圖表用，一次查詢多個商品名稱
	@PostMapping("/product/getProductsByIds")
    public List<ProductBean> getProductsByIds(@RequestBody List<Long> productIDs) {
        return pService.findProductsByIds(productIDs);
    }
	
	@GetMapping("/product/selectById/{productId}")
	public ResponseEntity<ProductBean> processQueryByIdAction(@PathVariable int productId) {
	    ProductBean product = pService.selectById(productId);
	    if (product == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(product);
	}


	@GetMapping("/product/getAllProduct")
	public List<ProductBean> processgetAllProduct() {
		return pService.getAllProduct();
	}
	
	// 查詢所有上架的產品
//    @GetMapping("/product/availableProduct")
//    public ResponseEntity<List<ProductBean>> getAvailableProducts() {
//        List<ProductBean> products = pService.getAllAvailableProducts();
//        System.out.println("測試"+products);
//        return ResponseEntity.ok(products);
//    }
	
	// 查詢所有上架的產品
//	@GetMapping("/product/availableProduct")
//	public ResponseEntity<List<ProductBean>> getAvailableProducts(@AuthenticationPrincipal Integer userId) {
//	    System.out.println("User ID: " + userId);
//	    ProfilesBean tmp=pfService.seleteById(userId);
//	    System.out.println("Hoooooooooooooooooooooooooooooooooooooooooooooooo:"+tmp.getAcct());
//	    
//
//	    List<ProductBean> products = pService.getAllAvailableProducts();
//	    return ResponseEntity.ok(products);
//	}
	@GetMapping("/product/availableProduct")
	public ResponseEntity<List<ProductBean>> getAvailableProducts() {
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    Integer userId = (Integer) authentication.getPrincipal(); // 获取 userId
//	    String acct = (String) authentication.getCredentials(); // 获取 acct

//	    System.out.println("Hoooooooooo User ID: " + userId);
//	    System.out.println("Hooooooooooo User Account: " + acct);

	    List<ProductBean> products = pService.getAllAvailableProducts();
	    return ResponseEntity.ok(products);
	}

    // 查詢所有下架的產品
    @GetMapping("/product/unavailableProduct")
    public ResponseEntity<List<ProductBean>> getUnavailableProducts() {
        List<ProductBean> products = pService.getAllUnavailableProducts();
        return ResponseEntity.ok(products);
    }

    // 查詢所有已假刪除的產品
    @GetMapping("/product/deletedProduct")
    public ResponseEntity<List<ProductBean>> getDeletedProducts() {
        List<ProductBean> products = pService.getAllDeletedProducts();
        return ResponseEntity.ok(products);
    }
	
	@DeleteMapping("/product/delete/{productId}") 
	public String processDeleteAction(@PathVariable int productId) {
		ProductBean queryBean = pService.selectById(productId);
		if(queryBean != null) {
			pService.deleteById(productId);
			return "Delete OK";
		}
		return "Delete not OK";
	}
		
	@PostMapping("/product/AddProduct")
	public ResponseEntity<String> processInsert(
	        @RequestParam("productName") String productName,
	        @RequestParam("productDescription") String productDescription,
	        @RequestParam("price") int price,
	        @RequestParam("stock") int stock,
	        @RequestParam("categoryId") int categoryId,
	        @RequestParam("photo") MultipartFile photo) {
	    ProductBean pBean = new ProductBean();
	    pBean.setProductName(productName);
	    pBean.setProductDescription(productDescription);
	    pBean.setPrice(price);
	    pBean.setStock(stock);
	    pBean.setCategoryId(categoryId);

	    try {
	        pService.insert(pBean, photo);
	        return ResponseEntity.ok("Insert OK");
	    } catch (IOException e) {
	        return ResponseEntity.status(500).body("Image upload failed");
	    }
	}

	 
	 @PostMapping("/product/bulkUpdateIsActive")
	    public ResponseEntity<?> bulkUpdateIsActive(@RequestBody BulkUpdateRequest request) {
	        try {
	            pService.bulkUpdateIsActive(request.getProductIDs(), request.getIsActive());
	            return ResponseEntity.ok(Map.of("message", "批量更新成功"));
	        } catch (Exception e) {
	            return ResponseEntity.status(500)
	                    .body(Map.of("error", "批量更新失敗", "details", e.getMessage()));
	        }
	    }
	 
	 @PutMapping("/product/updateIsActive/{productID}")
	    public String updateIsActive(@PathVariable Integer productID) {
	        if (pService.updateProductIsActive(productID)) {
	            return "Delete OK";
	        }
	        return "Delete not OK";
	    }
	 
	 
	 @PutMapping("/product/update/{productId}") 
		public String processUpdateAction(
				@PathVariable int productId,
				@RequestParam("productName") String productName,
	            @RequestParam("productDescription") String productDescription,
	            @RequestParam("price") int price,
	            @RequestParam("stock") int stock,
	            @RequestParam("categoryId") int categoryId,
	            @RequestParam("createdAt") LocalDateTime createdAt,
	            @RequestParam("isActive") int isActive,
	            @RequestParam("imageUrl") String imageUrl,
	            @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
		 
		 	ProductBean updateBean = new ProductBean();
		 	updateBean.setProductID(productId);
		 	updateBean.setProductName(productName);
		 	updateBean.setProductDescription(productDescription);
		 	updateBean.setPrice(price);
		 	updateBean.setStock(stock);
		 	updateBean.setCategoryId(categoryId);
		 	updateBean.setUpdatedAt(LocalDateTime.now());
		 	updateBean.setCreatedAt(createdAt);
		 	updateBean.setIsActive(isActive);
		 	updateBean.setImageUrl(imageUrl);
		 	
		 // 檢查是否有上傳圖片，如果有則處理圖片，否則保留現有圖片
		    if (photo != null && !photo.isEmpty()) {
		        // 如果有新的圖片，上傳並更新圖片
		        pService.update(updateBean, photo);
		    } else {
		        // 如果沒有新圖片，僅更新其他欄位
		        pService.updateWithoutPhoto(updateBean);
		    }	
			return "Update OK";
		}

	 
	
	
	
	
	


	
}
