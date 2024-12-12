package com.fileManager.service.impl;

import com.fileManager.entity.Files;
import com.fileManager.mapper.FilesMapper;
import com.fileManager.service.IFilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fileManager.vo.FileStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements IFilesService {
    @Autowired
    private FilesMapper filesMapper;

    @Override
    public List<Files> getFileTree(Long userId) {
        return filesMapper.getFileTree(userId);
    }

    @Override
    public boolean addFile(Files file) {
        return filesMapper.insert(file) > 0;
    }

    @Override
    public boolean deleteFile(Long fileId) {
        return filesMapper.deleteById(fileId) > 0;
    }

    @Override
    public boolean updateFileName(Long fileId, String newName) {
        Files file = filesMapper.selectById(fileId);
        if (file != null) {
            file.setFileName(newName);
            return filesMapper.updateById(file) > 0;
        }
        return false;
    }

    @Override
    public FileStats getFileStats(Long userId) {
        return filesMapper.getFileStats(userId);
    }

    @Override
    public List<Files> searchFilesByName(String fileName) {
        return filesMapper.searchFilesByName(fileName);
    }
}
