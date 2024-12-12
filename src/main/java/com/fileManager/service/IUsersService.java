package com.fileManager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fileManager.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    IPage<Users> getUserList(int page, int size);

    // 增加用户
    boolean addUser(Users user);

    // 批量删除用户
    boolean deleteUsers(List<Long> ids);

    // 修改用户信息
    boolean updateUser(Users user);
    Users getUserByUsername(String username);
    public Users getUserById(Long id);
}
