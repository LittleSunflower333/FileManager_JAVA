package com.fileManager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fileManager.entity.ApiLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApiLogMapper extends BaseMapper<ApiLog> {

    /**
     * 根据关键字进行模糊查询，并按时间倒序分页
     * @param page 分页对象
     * @param search 模糊查询的关键字
     * @return 返回分页后的日志列表
     */
    @Select("<script>" +
            "SELECT * FROM api_logs" +
            " <where>" +
            "   <if test='search != null and search != \"\"'>" +
            "     AND (username LIKE CONCAT('%', #{search}, '%')" +
            "     OR ip LIKE CONCAT('%', #{search}, '%')" +
            "     OR url LIKE CONCAT('%', #{search}, '%')" +
            "     OR method LIKE CONCAT('%', #{search}, '%')" +
            "     OR params LIKE CONCAT('%', #{search}, '%')" +
            "     OR result LIKE CONCAT('%', #{search}, '%')" +
            "     OR error LIKE CONCAT('%', #{search}, '%'))" +
            "   </if>" +
            " </where>" +
            " ORDER BY created_at DESC" +
            "</script>")
    Page<ApiLog> selectLogsBySearch(Page<ApiLog> page, @Param("search") String search);

}
