package com.fileManager.controller;

import com.fileManager.entity.ApiLog;
import com.fileManager.service.IApiLogService;
import com.fileManager.util.ApiResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/apiLog")
public class ApiLogController {

    @Autowired
    private IApiLogService apiLogService;

    /**
     * 获取所有日志列表，支持模糊查询和分页
     * @param page 当前页码
     * @param size 每页显示的记录数
     * @param search 需要模糊查询的日志内容
     * @return 日志分页列表
     */
    @GetMapping("/list")
    public ApiResponse<Page<ApiLog>> getLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        // 创建分页对象
        Page<ApiLog> apiLogPage = new Page<>(page, size);

        // 调用 service 层进行模糊查询和分页查询
        Page<ApiLog> resultPage = apiLogService.getLogsBySearch(apiLogPage, search);

        // 返回分页结果
        return ApiResponse.success(resultPage);
    }

    @PostMapping("/import")
    public String importLogs(@RequestParam("file") MultipartFile file, @RequestParam("isSafe") boolean isSafe) {
        try {
            List<ApiLog> logs = apiLogService.importLogsFromXml(file, isSafe);
            return "Logs imported successfully: " + logs.toString();
        } catch (Exception e) {
            e.printStackTrace();
            if (!isSafe) {
                return "Error: Potential XXE attack detected. " + e.getMessage();
            }
            return "Log import failed: " + e.getMessage();
        }
    }

}
