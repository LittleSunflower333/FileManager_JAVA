package com.fileManager.service;

import com.fileManager.util.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileService {
    @Value("${file.storage.path}")
    private String fileBasePath;
    /**
     * 上传文件并返回存储路径和新文件名
     *
     * @param file MultipartFile 对象
     * @return 包含新文件名和完整存储路径的 Map
     * @throws IOException 文件存储时的异常
     */
    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("文件为空，上传失败");
        }
        String path = fileBasePath+UserContext.getCurrentUser();
        // 确保存储目录存在
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 获取文件原始名称和扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = ""; // 文件扩展名
        String baseName = originalFilename; // 文件名基础部分

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        // 生成新的文件名：原始名称 + 时间戳
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String newFileName = baseName + "_" + timestamp + extension;

        String filePath = path +"/"+ newFileName;

        // 将文件保存到本地
        Files.write(Paths.get(filePath), file.getBytes());

        // 返回文件名和存储路径
        Map<String, String> result = new HashMap<>();
        result.put("newFileName", newFileName);
        result.put("filePath", filePath);

        return result;
    }
    /**
     * 下载文件
     *
     * @param filePath 文件存储路径
     * @param response HttpServletResponse，用于写回文件
     * @throws IOException 文件读取或写入时的异常
     */
    public void downloadFile(String filePath, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在：" + filePath);
        }

        // 设置响应头
        response.setContentType(Files.probeContentType(file.toPath())); // 自动设置文件类型
        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setContentLength((int) file.length());

        // 将文件写入响应输出流
        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}

