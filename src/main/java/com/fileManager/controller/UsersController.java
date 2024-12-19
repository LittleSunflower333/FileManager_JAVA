package com.fileManager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fileManager.entity.Users;
import com.fileManager.service.IUsersService;
import com.fileManager.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/hello")
    public ResponseEntity<?> getHello() {
        return ResponseEntity.ok("hello");
    }
    // 分页查找用户信息
    @GetMapping("/list")
    public ResponseEntity<?> getUserList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        IPage<Map<String, Object>> userList = usersService.getUserList(page, size);
        return ResponseEntity.ok(userList);
    }

    // 增加用户
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        boolean result = usersService.addUser(user)!=null;
        return result
                ? ResponseEntity.ok("用户添加成功")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户添加失败");
    }

    // 批量删除用户
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUsers(@RequestBody List<String> ids) {
        boolean result = usersService.deleteUsers(ids);
        return result
                ? ResponseEntity.ok("用户删除成功")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户删除失败");
    }

    // 修改用户信息
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Users user) {
        boolean result = usersService.updateUser(user);
        return result
                ? ResponseEntity.ok("用户信息更新成功")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户信息更新失败");
    }

    // 根据用户ID获取用户信息
    @GetMapping("/getLoginUserInfo")
    public ResponseEntity<?> getUserById() {
        Users user = usersService.getUserById(UserContext.getCurrentUser());
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户不存在");
    }
}
