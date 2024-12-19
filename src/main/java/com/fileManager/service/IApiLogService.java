package com.fileManager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fileManager.entity.ApiLog;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IApiLogService extends IService<ApiLog> {
    Page<ApiLog> getLogsBySearch(Page<ApiLog> page, String search);
    List<ApiLog>  importLogsFromXml(MultipartFile file, boolean isSafe) throws Exception;
}
