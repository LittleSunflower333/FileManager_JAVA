package com.fileManager.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fileManager.entity.Files;
import com.fileManager.vo.FileStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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


    // 获取文件数量和总大小
    @Select("SELECT COUNT(*) AS totalCount, SUM(file_size) AS totalSize " +
            "FROM files WHERE uploaded_by = #{userId}")
    FileStats getFileStats(@Param("userId") String userId);
    // 根据文件名进行模糊搜索
    @Select("SELECT * FROM files WHERE file_name LIKE CONCAT('%', #{fileName}, '%')")
    List<Files> searchFilesByName(@Param("fileName") String fileName);
}
