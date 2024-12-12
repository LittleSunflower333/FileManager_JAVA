package com.fileManager.controller;

import com.fileManager.service.IFoldersService;
import com.fileManager.entity.Folders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/folders")
public class FoldersController {

    @Autowired
    private IFoldersService foldersService;

    // 增加文件夹
    @PostMapping("/add")
    public String addFolder(@RequestBody Folders folder) {
        boolean result = foldersService.addFolder(folder);
        return result ? "Folder added successfully" : "Failed to add folder";
    }

    // 删除文件夹
    @DeleteMapping("/delete/{id}")
    public String deleteFolder(@PathVariable Long id) {
        boolean result = foldersService.deleteFolder(id);
        return result ? "Folder deleted successfully" : "Failed to delete folder";
    }

    // 修改文件夹
    @PutMapping("/update")
    public String updateFolder(@RequestBody Folders folder) {
        boolean result = foldersService.updateFolder(folder);
        return result ? "Folder updated successfully" : "Failed to update folder";
    }
}
