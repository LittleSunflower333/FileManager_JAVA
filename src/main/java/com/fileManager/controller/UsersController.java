package com.fileManager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fileManager.entity.Users;
import com.fileManager.service.IUsersService;
import com.fileManager.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private IUsersService usersService;

    // 分页查找用户信息
    @GetMapping("/list")
    public ApiResponse<IPage<Users>> getUserList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        IPage<Users> userPage = usersService.getUserList(page, size);
        return ApiResponse.success(userPage);
    }

    // 增加用户
    @PostMapping("/add")
    public ApiResponse<String> addUser(@RequestBody Users user) {
        boolean result = usersService.addUser(user);
        return result ? ApiResponse.success("用户添加成功") : ApiResponse.error("用户添加失败");
    }

    // 批量删除用户
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUsers(@RequestBody List<Long> ids) {
        boolean result = usersService.deleteUsers(ids);
        return result ? ApiResponse.success("用户删除成功") : ApiResponse.error("用户删除失败");
    }

    // 修改用户信息
    @PutMapping("/update")
    public ApiResponse<String> updateUser(@RequestBody Users user) {
        boolean result = usersService.updateUser(user);
        return result ? ApiResponse.success("用户信息更新成功") : ApiResponse.error("用户信息更新失败");
    }
    // 根据用户ID获取用户信息
    @GetMapping("/{id}")
    public ApiResponse<Users> getUserById(@PathVariable Long id) {
        Users user = usersService.getUserById(id);
        return user != null ? ApiResponse.success(user) : ApiResponse.error("用户不存在");
    }
}
