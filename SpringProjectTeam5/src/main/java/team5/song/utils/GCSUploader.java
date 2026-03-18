package team5.song.utils;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GCSUploader {
    private static final Storage storage = StorageOptions.getDefaultInstance().getService();
    /**
     * @param localDirectory hls的本地檔案路徑，要上傳的資料的"資料夾"(不用填index.m3u8)
     */
    public static String uploadFileToGCS(String bucketName,String localDirectory, String songID) {
        try {
            // 上傳 .m3u8 檔案
            uploadFile(bucketName, "newMusicData/"+songID+"/hls/index.m3u8", localDirectory + "/index.m3u8");
            // 上傳 .ts 檔案
            File directory = new File(localDirectory);
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".ts"));
            if (files != null) {
                for (File file : files) {
                    uploadFile(bucketName, "newMusicData/"+songID+"/hls/" + file.getName(), file.getAbsolutePath());
                }
                return "newMusicData/"+songID+"/hls/";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";

    }

    protected static void uploadFile(String bucketName, String objectName, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/octet-stream").build();

        storage.create(blobInfo, bytes);

        System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

}
