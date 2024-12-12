package com.fileManager.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fileManager.entity.Files;
import com.fileManager.vo.FileStats;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
@Mapper
public interface FilesMapper extends BaseMapper<Files> {
    // 获取文件树形结构
    List<Files> getFileTree(@Param("userId") Long userId);

    // 获取文件数量和总大小
    FileStats getFileStats(@Param("userId") Long userId);

    // 根据文件名查找文件列表
    List<Files> searchFilesByName(@Param("name") String fileName);
}
