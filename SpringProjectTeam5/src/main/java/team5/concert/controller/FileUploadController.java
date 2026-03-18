package team5.concert.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
//@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Value("${photo.storage.prefix}")
    private String uploadDir;  // 讀取設定的上傳路徑

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未選擇文件");
        }

        try {
            // 取得文件原始名稱
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            System.out.println("File name: " + fileName);  // 輸出檔案名稱
            
            // 防止路徑穿越攻擊
            if (fileName.contains("..")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文件名包含非法字符");
            }

            // 確保上傳目錄存在
            Path path = Paths.get( uploadDir + "concert/");
            if (!Files.exists(path)) {
                Files.createDirectories(path);  // 創建目錄（如果不存在
                System.out.println("Created directory: " + path);  // 輸出創建的目錄）
            }
            
         // 如果文件已存在，為文件名添加時間戳
            Path filePath = path.resolve(fileName);
            if (Files.exists(filePath)) {
                String timeStamp = String.valueOf(System.currentTimeMillis());
                filePath = path.resolve(timeStamp + "-" + fileName);  // 使用時間戳來避免文件名衝突
            }

            // 保存文件
            Files.copy(file.getInputStream(), filePath);

            // 返回圖片的URL（假設圖片可以通過 http://localhost:8080/img/concert/<file_name> 訪問）
            String photoUrl = "/img/concert/" + fileName;
            System.out.println("Generated Image URL: " + photoUrl);  // 打印生成的 URL
            return ResponseEntity.status(HttpStatus.OK).body(photoUrl);
        } catch (IOException e) {
        	e.printStackTrace();  // 輸出異常堆疊信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上傳失敗：" + e.getMessage());
        }
    }
}
