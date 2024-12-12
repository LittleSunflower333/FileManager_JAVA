package com.fileManager.mapper;

import com.fileManager.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    @Select("SELECT * FROM users WHERE username = #{username}")
    Users selectByUsername(String username);

}
