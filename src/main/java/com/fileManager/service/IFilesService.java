package com.fileManager.service;

import com.fileManager.entity.Files;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fileManager.vo.FileStats;

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
    // 获取文件树形结构
    List<Files> getFileTree(Long userId);

    // 新增文件
    boolean addFile(Files file);

    // 删除文件
    boolean deleteFile(Long fileId);

    // 修改文件名
    boolean updateFileName(Long fileId, String newName);

    // 获取当前用户的文件数量和文件总大小
    FileStats getFileStats(Long userId);

    // 根据文件名查找文件列表
    List<Files> searchFilesByName(String fileName);
}
