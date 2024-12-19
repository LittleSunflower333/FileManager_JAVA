package com.fileManager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.fileManager.entity.Users;
import com.fileManager.mapper.UsersMapper;
import com.fileManager.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fileManager.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    @Autowired
    private FoldersServiceImpl foldersService;

    @Override
    public IPage<Map<String, Object>> getUserList(int page, int size) {
        IPage<Map<String, Object>> userMapsPage = usersMapper.selectMapsPage(
                new Page<>(page, size),
                new QueryWrapper<Users>().select("id", "username", "email", "created_at", "updated_at", "status")
        );
        return userMapsPage;
    }


    // 增加用户
    @Override
    public String addUser(Users user) {
         this.save(user);
        return user.getId();
    }

    // 批量删除用户
    @Override
    public boolean deleteUsers(List<String> ids) {
        return usersMapper.deleteBatchIds(ids) > 0;
    }

    // 修改用户信息
    @Override
    public boolean updateUser(Users user) {
        return this.updateById(user) ;
    }
    @Override
    public Users getUserByUsernameAndPwd(String username,String pwd,boolean isSafe){
        //加密
        pwd = PasswordUtil.encode(pwd);
        if(isSafe){
            //安全防注入
            return usersMapper.selectByUsernameAndPwdSafe(username,pwd);
        }else {
            // 含有 SQL 注入漏洞的部分
            // 假设这里通过直接拼接用户名和密码来构建 SQL 查询，容易受到 SQL 注入攻击
            String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + pwd + "'";

            // 执行查询
            return usersMapper.selectByRawSql(sql).get(0);
        }
    }
    @Override
    public Users getUserByUsername(String username){
        return usersMapper.selectByUsername(username);
    }
    // 根据用户 ID 获取用户信息
    @Override
    public Users getUserById(String id) {
        // 获取用户信息
        Users user = usersMapper.selectById(id);
        user.setPassword(null);
        return user;
    }
}
