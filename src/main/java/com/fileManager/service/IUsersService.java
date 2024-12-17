package com.fileManager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fileManager.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
public interface IUsersService extends IService<Users> {
    // 分页查找用户信息
    IPage<Map<String, Object>> getUserList(int page, int size);

    // 增加用户
    String addUser(Users user);

    // 批量删除用户
    boolean deleteUsers(List<String> ids);

    // 修改用户信息
    boolean updateUser(Users user);
    Users getUserByUsernameAndPwd(String username,String pwd,boolean isSafe);
    Users getUserByUsername(String username);
    public Users getUserById(String id);
}
