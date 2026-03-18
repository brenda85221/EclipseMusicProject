package team5.product.model;

import java.util.Objects;
import java.util.UUID;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service.key}")
    private String serviceKey;

    private final OkHttpClient client = new OkHttpClient();

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        String fileName = UUID.randomUUID() + "_" +
            Objects.requireNonNull(file.getOriginalFilename())
                .replaceAll("[^a-zA-Z0-9._-]", "");
        String filePath = folder + "/" + fileName;
        String uploadUrl = supabaseUrl + "/storage/v1/object/product-images/" + filePath;

        RequestBody requestBody = RequestBody.create(
            file.getBytes(),
            MediaType.parse(file.getContentType())
        );

        Request request = new Request.Builder()
            .url(uploadUrl)
            .post(requestBody)
            .addHeader("Authorization", "Bearer " + serviceKey)
            .addHeader("Content-Type", file.getContentType())
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("上傳失敗: " + response.code() + " " + response.message());
            }
            return supabaseUrl + "/storage/v1/object/public/product-images/" + filePath;
        }
    }

    public boolean deleteImage(String imageUrl) throws IOException {
        String filePath = imageUrl.substring(
            imageUrl.lastIndexOf("/public/product-images/") + "/public/product-images/".length()
        );
        String deleteUrl = supabaseUrl + "/storage/v1/object/product-images/" + filePath;
        Request request = new Request.Builder()
            .url(deleteUrl)
            .delete()
            .addHeader("Authorization", "Bearer " + serviceKey)
            .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
    
    @PostConstruct
    public void debugValue() {
        System.out.println("supabaseUrl = " + supabaseUrl);
        System.out.println("serviceKey = " + serviceKey);
    }
}
