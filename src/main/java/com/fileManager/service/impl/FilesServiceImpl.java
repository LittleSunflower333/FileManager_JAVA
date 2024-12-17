package com.fileManager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fileManager.entity.Files;
import com.fileManager.mapper.FilesMapper;
import com.fileManager.service.FileService;
import com.fileManager.service.IFilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fileManager.util.UserContext;
import com.fileManager.vo.FileStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements IFilesService {
    @Autowired
    private FilesMapper filesMapper;
    @Autowired
    private FileService fileService;


    @Override
    public boolean addFile(MultipartFile file, String folderId, boolean isSafe) throws IOException {
        if (isSafe) {
            // 执行文件安全检查
            if (!isValidFile(file)) {
                throw new IOException("文件类型不允许或文件过大");
            }
            if (!isSafeFileContent(file)) {
                throw new IOException("文件内容包含潜在恶意代码或不安全");
            }


            // 你可以在这里添加更多的文件内容安全扫描逻辑，检查文件是否包含恶意代码等
        }

        // 调用上传文件方法，获取文件信息
        Map<String, String> fileInfoMap = fileService.uploadFile(file);

        String originalFilename = file.getOriginalFilename();
        String extension = ""; // 默认扩展名为空
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 创建 Files 对象并赋值
        Files newFile = new Files();
        newFile.setFileName(fileInfoMap.get("newFileName"));
        newFile.setFilePath(fileInfoMap.get("filePath"));
        newFile.setFileSize((int) file.getSize());
        newFile.setUploadedBy(UserContext.getCurrentUser());
        newFile.setFileType(extension);
        newFile.setFolderId(folderId);

        return this.save(newFile);
    }

    @Override
    public void getFile(String fileId, HttpServletResponse response) throws IOException {
        Files byId = this.getById(fileId);
        fileService.downloadFile(byId.getFilePath(),response);
    }

    @Override
    public boolean deleteFile(String fileId) {
        return filesMapper.deleteById(fileId) > 0;
    }

    @Override
    public FileStats getFileStats(String userId) {
        return filesMapper.getFileStats(userId);
    }

    @Override
    public List<Files> searchFilesByName(String fileName) {
        return filesMapper.searchFilesByName(fileName);
    }
    @Override
    public List<Files> searchFilesByNameForUser(String fileName, String userId) {
        // 根据文件名和当前用户 ID 模糊查询文件
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("file_name", fileName)  // 模糊查询文件名
                .eq("uploaded_by", userId);   // 确保文件属于当前用户
        return filesMapper.selectList(queryWrapper);
    }
    private boolean isValidFile(MultipartFile file) {
        // 限制文件类型
        String[] allowedExtensions = {".jpg", ".png", ".pdf", ".txt", ".docx"};
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 检查文件扩展名是否允许
        boolean isAllowedExtension = Arrays.asList(allowedExtensions).contains(extension.toLowerCase());

        // 限制文件大小（例如：最大10MB）
        long maxSize = 10 * 1024 * 1024;  // 10MB
        boolean isSizeValid = file.getSize() <= maxSize;

        return isAllowedExtension && isSizeValid;
    }
    private boolean isSafeFileContent(MultipartFile file) throws IOException {
        // 1. 黑名单检测：检测文件内容中的危险关键字
        List<String> dangerousKeywords = Arrays.asList(
                "<script>", "</script>", "<?php", "eval(", "onerror=", "onclick="
        );

        // 读取文件内容（限制大小以避免内存问题）
        byte[] fileBytes = file.getBytes();
        String fileContent = new String(fileBytes, StandardCharsets.UTF_8);

        for (String keyword : dangerousKeywords) {
            if (fileContent.toLowerCase().contains(keyword)) {
                return false; // 如果发现危险关键字，返回不安全
            }
        }

        // 2. MIME 类型验证
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String expectedMimeType = java.nio.file.Files.probeContentType(Paths.get(originalFilename));
            String actualMimeType = file.getContentType();

            if (expectedMimeType != null && !expectedMimeType.equals(actualMimeType)) {
                return false; // 如果 MIME 类型不匹配，返回不安全
            }
        }

        // 3. 检查是否包含可执行文件签名（防止上传二进制可执行文件）
        if (containsExecutable(fileBytes)) {
            return false;
        }

        // 4. 文件大小限制（可选，通常在外层进行）
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            return false;
        }

        // 如果所有检查通过，文件内容安全
        return true;
    }
    private boolean containsExecutable(byte[] fileBytes) {
        // 常见的可执行文件签名
        String[] exeSignatures = {
                "MZ",  // Windows PE文件（例如 .exe, .dll）
                "\u0007\u0001\u0001", // ELF文件
                "#!",  // Unix/Linux脚本文件
                "PK"   // ZIP格式（可能是恶意伪装的 .jar 文件）
        };

        String header = new String(Arrays.copyOfRange(fileBytes, 0, 4), StandardCharsets.UTF_8);

        for (String signature : exeSignatures) {
            if (header.startsWith(signature)) {
                return true; // 如果文件头部包含可执行签名，返回不安全
            }
        }

        return false;
    }




}
