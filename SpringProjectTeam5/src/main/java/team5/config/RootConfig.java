package team5.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Configuration
public class RootConfig implements WebMvcConfigurer {

	// allowCredentials(允許發送跨域請求時帶上憑證) 設為 true 時，origins 不能設為 "*"
    // Spring MVC 的 CORS 配置
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("https://chenyu-musicweb.netlify.app", "https://brendamanagemusicweb.netlify.app","http://localhost:5174") // 添加你的前端 URL
				.allowCredentials(true) // 允許帶有憑證的請求
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
				.allowedHeaders("*"); // 允許的請求頭
//		        .exposedHeaders("Authorization", "Content-Type"); //顯式允許 multipart/form-data
//		.exposedHeaders("Authorization") // 如果有需要前端獲取自定義的 header
//        .maxAge(3600); // 預檢請求的緩存時間
	}
	
	// 配置 Google Cloud Storage (GCS)
    @Bean
    public Storage googleStorage() {
        return StorageOptions.getDefaultInstance().getService();  // 返回 Google Cloud Storage 服務實例
    }

}


/*
 * // 根據邏輯返回允許的來源 private String[] getAllowedOrigins() { // 根據邏輯動態生成可允許的網域列表
 * return new String[] {"http://localhost:5173", "http://localhost:5174"}; }
 */