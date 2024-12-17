package com.fileManager.controller;

import com.fileManager.entity.Folders;
import com.fileManager.service.IFilesService;
import com.fileManager.entity.Files;
import com.fileManager.service.IFoldersService;
import com.fileManager.util.OperatingSystemUtils;
import com.fileManager.util.UserContext;
import com.fileManager.vo.FileStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private IFilesService filesService;
    @Autowired
    private IFoldersService foldersService;

    // 获取当前用户的文件夹树形结构
    @GetMapping("/getFileTree")
    public ResponseEntity<?> getFileTree() {
        List<Folders> foldersTreeByUserId = foldersService.getFoldersTreeByUserId();
        return foldersTreeByUserId != null
                ? ResponseEntity.ok(foldersTreeByUserId)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("File tree not found");
    }

    /**
     * 新增文件
     *
     * @param file 文件上传的对象
     * @param folderId  所属文件夹
     * @return ResponseEntity
     */
    @PostMapping("/add")
    public ResponseEntity<?> addFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam("folderId") String folderId) {
        try{
            boolean result = filesService.addFile(file, folderId);
            return result
                    ? ResponseEntity.ok("File added successfully")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add file");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add file");
        }

    }


    @PostMapping("/execute")
    public ResponseEntity<?> executeCommand(@RequestBody Map<String, Object> params
   ) {
        try {
            String command = params.get("command").toString();
            boolean isSafe = (boolean) params.get("isSafe");
            List<String> commandList;

            if (isSafe) {
                // 安全执行命令
                commandList = getSafeCommand(command);
            } else {
                // 不安全执行命令
                commandList = getUnsafeCommand(command);
            }

            if (commandList == null) {
                return ResponseEntity.ok().body("Invalid command");
            }

            // 执行命令
            ProcessBuilder processBuilder = new ProcessBuilder(commandList);
            processBuilder.redirectErrorStream(true); // 合并标准输出和错误输出
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            return ResponseEntity.ok("Command executed:\n" + command + "\nOutput:\n" + output.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    private List<String> getSafeCommand(String command) {
        String os = OperatingSystemUtils.getOperatingSystem(); // 获取当前操作系统

        // 根据操作系统判断执行命令
        if (os.contains("win")) {
            // Windows 系统下的安全命令
            if (command.equals("dir")) {
                return Arrays.asList("cmd.exe", "/C", "dir");
            } else if (command.equals("echo")) {
                return Arrays.asList("cmd.exe", "/C", "echo Hello");
            }
        } else {
            // Linux 系统下的安全命令
            if (command.equals("ls")) {
                return Arrays.asList("ls", "-l");
            } else if (command.equals("pwd")) {
                return Arrays.asList("pwd");
            } else if (command.equals("echo")) {
                return Arrays.asList("echo", "Hello");
            }
        }
        return null; // 如果没有匹配的安全命令
    }

    private List<String> getUnsafeCommand(String command) {
        String os = OperatingSystemUtils.getOperatingSystem(); // 获取当前操作系统

        // 根据操作系统判断执行命令
        if (os.contains("win")) {
            // Windows 系统下的不安全命令
            if (command != null && !command.trim().isEmpty()) {
                return Arrays.asList("cmd.exe", "/C", command);
            }
        } else {
            // Linux 系统下的不安全命令
            if (command != null && !command.trim().isEmpty()) {
                return Arrays.asList("/bin/sh", "-c", command);
            }
        }
        return null; // 如果没有有效命令
    }

    // 添加下载文件的接口
    @GetMapping("/download/{fileId}")
    public void downloadFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        try {
            filesService.getFile(fileId, response);
        } catch (IOException e) {
            // 如果下载失败，返回错误响应
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Failed to download file");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    // 删除文件
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileId") String fileId) {
        boolean result = filesService.deleteFile(fileId);
        return result
                ? ResponseEntity.ok("File deleted successfully")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file");
    }



    // 获取当前用户的所有文件数量和文件总大小
    @GetMapping("/getFileStats/{userId}")
    public ResponseEntity<?> getFileStats(@PathVariable("userId") String userId) {
        FileStats stats = filesService.getFileStats(userId);
        return stats != null
                ? ResponseEntity.ok(stats)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("File stats not found");
    }

    // 根据文件名查找文件列表
    @GetMapping("/searchFilesByName")
    public ResponseEntity<?> searchFilesByName(@RequestParam("name") String fileName) {
        List<Files> files = filesService.searchFilesByName(fileName);
        return files != null && !files.isEmpty()
                ? ResponseEntity.ok(files)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No files found");
    }
    // 根据文件名模糊搜索当前用户的文件列表
    @GetMapping("/searchFilesByNameofLoginUser")
    public ResponseEntity<?> searchFilesByNameofLoginUser(@RequestParam("fileName") String fileName) {
        try {
            // 获取当前用户的 ID
            String currentUserId = UserContext.getCurrentUser();

            // 使用文件服务根据文件名和当前用户 ID 搜索文件
            List<Files> files = filesService.searchFilesByNameForUser(fileName, currentUserId);

            return  ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
