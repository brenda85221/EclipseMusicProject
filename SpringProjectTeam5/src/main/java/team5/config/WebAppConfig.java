package team5.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {
	
	@Value("${photo.storage.prefix}")
	private String photoPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 設定靜態資源對應
        registry.addResourceHandler("/img/**")
        		.addResourceLocations("file:" + photoPath);
//        		.addResourceLocations("file:///" + photoPath);
//                .addResourceLocations(photoPath);
    }
	
	//
//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/img/**")
//                .allowedOrigins("http://localhost:5173") // 允許來自前端的請求
//                .allowedMethods("GET");
//    }
}
