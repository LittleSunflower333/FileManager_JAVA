package com.fileManager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fileManager.entity.ApiLog;
import com.fileManager.mapper.ApiLogMapper;
import com.fileManager.service.IApiLogService;
import org.springframework.stereotype.Service;

@Service
public class ApiLogServiceImpl extends ServiceImpl<ApiLogMapper, ApiLog> implements IApiLogService {

    @Override
    public Page<ApiLog> getLogsBySearch(Page<ApiLog> page, String search) {
        // 进行模糊查询，按时间倒序排序
        return baseMapper.selectLogsBySearch(page, search);
    }
}
