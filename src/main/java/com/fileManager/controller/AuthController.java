package com.fileManager.controller;

import com.fileManager.entity.Folders;
import com.fileManager.entity.Users;
import com.fileManager.service.IFoldersService;
import com.fileManager.service.IUsersService;
import com.fileManager.util.JwtUtil;
import com.fileManager.util.PasswordUtil;
import com.fileManager.vo.LoginRequest;
import com.fileManager.vo.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final IUsersService usersService;
    private final IFoldersService foldersService;

    public AuthController(JwtUtil jwtUtil, IUsersService usersService, IFoldersService foldersService) {
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
        this.foldersService = foldersService;
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 验证用户名和密码
        Users userNow = usersService.getUserByUsernameAndPwd(loginRequest.getUsername(),loginRequest.getPassword(),loginRequest.getIsSafe());
        if (userNow != null) {
            // 生成 Token
            String token = jwtUtil.generateToken(userNow.getId());
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // 注册接口
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        // 检查用户名是否已存在
        if (usersService.getUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // 密码加密
        user.setPassword(PasswordUtil.encode(user.getPassword()));

        // 保存用户信息
        String userId = usersService.addUser(user);
        if (userId != null) {
            // 创建用户的根目录
            Folders folders = new Folders();
            folders.setFolderName("根目录");
            foldersService.addFolder(folders, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }
}
