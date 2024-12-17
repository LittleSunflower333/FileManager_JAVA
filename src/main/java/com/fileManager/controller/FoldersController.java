package com.fileManager.controller;

import com.fileManager.service.IFoldersService;
import com.fileManager.entity.Folders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FoldersController {

    @Autowired
    private IFoldersService foldersService;

    // 增加文件夹
    @PostMapping("/add")
    public ResponseEntity<?> addFolder(@RequestBody Folders folder) {
        boolean result = foldersService.addFolder(folder,null);
        return result
                ? ResponseEntity.ok("Folder added successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add folder");
    }

    // 删除文件夹
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable String id) {
        boolean result = foldersService.deleteFolder(id);
        return result
                ? ResponseEntity.ok("Folder deleted successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete folder");
    }

    // 修改文件夹
    @PutMapping("/update")
    public ResponseEntity<?> updateFolder(@RequestBody Folders folder) {
        boolean result = foldersService.updateFolder(folder);
        return result
                ? ResponseEntity.ok("Folder updated successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update folder");
    }

}
