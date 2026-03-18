package team5.song.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    // 將檔案儲存到指定路徑的方法
    public static void uploadFileToLocation(Path path) {

    }

    // 遞歸地刪除目錄及其所有內容的方法
    public static void deleteDirectory(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectory(entry);
                }
            }
        }
        Files.delete(path);
    }

    // 轉檔成串流格式
    public static boolean toFfmpeg(String inputPath, String outputPath) throws IOException {
        System.out.println("inputPath: " + inputPath);
        String[] ffmpegCommand = {
            "cmd.exe", "/c", "ffmpeg", "-i", inputPath, "-codec", "copy", "-start_number", "0",
            "-hls_time", "10",
            "-hls_list_size", "0", "-f", "hls", outputPath + "/index.m3u8"
        };


        // 檢查並創建輸出目錄
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            if (outputDir.mkdirs()) {
                System.out.println("輸出目錄已成功創建！");
            } else {
                System.err.println("創建輸出目錄失敗！");
                return false;
            }
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpegCommand);
            processBuilder.environment().put("LC_ALL", "C.UTF-8"); // 設定 UTF-8
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 捕捉並輸出FFmpeg命令的日誌
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("FFmpeg命令執行成功！");
                return true;
            } else {
                System.err.println("FFmpeg命令執行失敗，退出碼：" + exitCode);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Long getVideoDuration(String inputPath) throws IOException {
    	System.out.println("inputPath: " + inputPath);
    	String ffprobePath = "\"C:\\Program Files\\FFMPEG\\bin\\ffprobe.exe\"";
        String[] ffprobeCommand = {
                "cmd.exe", "/c", "ffprobe", "-v", "error", "-show_entries",
                "format=duration", "-of", "default=noprint_wrappers=1:nokey=1", inputPath
        };

        System.out.println("ffprobeCommand Start");
        for(String s:ffprobeCommand) {
        	System.out.println(s);
        }
        System.out.println("ffprobeCommand End");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(ffprobeCommand);
            processBuilder.environment().put("LC_ALL", "C.UTF-8"); // 設定 UTF-8
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null) {
                    double durationInSeconds = Double.parseDouble(line.trim());
                    return (long) durationInSeconds;
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("FFprobe命令執行失敗，退出碼：" + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return 0L;
    }

}
