package com.fileManager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fileManager.entity.Users;
import com.fileManager.mapper.UsersMapper;
import com.fileManager.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    @Autowired
    private UsersMapper usersMapper;

    // 分页查找用户信息
    @Override
    public IPage<Users> getUserList(int page, int size) {
        IPage<Users> userPage = new Page<>(page, size);
        return usersMapper.selectPage(userPage, null); // 这里可以加上条件过滤器
    }

    // 增加用户
    @Override
    public boolean addUser(Users user) {
        return usersMapper.insert(user) > 0;
    }

    // 批量删除用户
    @Override
    public boolean deleteUsers(List<Long> ids) {
        return usersMapper.deleteBatchIds(ids) > 0;
    }

    // 修改用户信息
    @Override
    public boolean updateUser(Users user) {
        return usersMapper.updateById(user) > 0;
    }
    @Override
    public Users getUserByUsername(String username) {
        return usersMapper.selectByUsername(username);
    }
    // 根据用户 ID 获取用户信息
    @Override
    public Users getUserById(Long id) {
        return usersMapper.selectById(id);
    }
}
