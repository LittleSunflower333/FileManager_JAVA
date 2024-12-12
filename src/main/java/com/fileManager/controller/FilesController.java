package com.fileManager.controller;

import com.fileManager.service.IFilesService;
import com.fileManager.entity.Files;
import com.fileManager.vo.FileStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private IFilesService filesService;

    // 获取当前用户的文件夹树形结构
    @GetMapping("/getFileTree/{userId}")
    public List<Files> getFileTree(@PathVariable("userId") Long userId) {
        return filesService.getFileTree(userId);
    }

    // 新增文件
    @PostMapping("/add")
    public boolean addFile(@RequestBody Files file) {
        return filesService.addFile(file);
    }

    // 删除文件
    @DeleteMapping("/delete/{fileId}")
    public boolean deleteFile(@PathVariable("fileId") Long fileId) {
        return filesService.deleteFile(fileId);
    }

    // 修改文件名
    @PutMapping("/updateName/{fileId}")
    public boolean updateFileName(@PathVariable("fileId") Long fileId, @RequestBody String newName) {
        return filesService.updateFileName(fileId, newName);
    }

    // 获取当前用户的所有文件数量和文件总大小
    @GetMapping("/getFileStats/{userId}")
    public FileStats getFileStats(@PathVariable("userId") Long userId) {
        return filesService.getFileStats(userId);
    }

    // 根据文件名查找文件列表
    @GetMapping("/searchByName")
    public List<Files> searchFilesByName(@RequestParam("name") String fileName) {
        return filesService.searchFilesByName(fileName);
    }
}
