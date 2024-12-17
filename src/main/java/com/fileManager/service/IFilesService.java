package com.fileManager.service;

import com.fileManager.entity.Files;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fileManager.vo.FileStats;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
public interface IFilesService extends IService<Files> {

    // 新增文件
    boolean addFile(MultipartFile file, String folderId, boolean isSafe) throws IOException;
    void getFile(String fileId, HttpServletResponse response) throws IOException;
    // 删除文件
    boolean deleteFile(String fileId);


    // 获取当前用户的文件数量和文件总大小
    FileStats getFileStats(String userId);

    // 根据文件名查找文件列表
    List<Files> searchFilesByName(String fileName);
    // 根据文件名和当前用户 ID 模糊查询文件列表
    List<Files> searchFilesByNameForUser(String fileName, String userId);
}
