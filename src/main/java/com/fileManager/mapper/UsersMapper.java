package com.fileManager.mapper;

import com.fileManager.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface UsersMapper extends BaseMapper<Users> {


    /***
     * 修复该漏洞的一种方法是使用预编译 SQL（参数化查询），
     * 这样可以确保用户输入不会直接影响 SQL 查询的结构。
     * @param username
     * @param pwd
     * @return
     */
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{pwd}")
    Users selectByUsernameAndPwdSafe(String username, String pwd);

    @Select("SELECT * FROM users WHERE username = #{username} ")
    Users selectByUsername(String username);

    @Select("${sql}")
    List<Users> selectByRawSql(@Param("sql") String sql);

}
