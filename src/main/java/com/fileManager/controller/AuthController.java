package com.fileManager.controller;

import com.fileManager.entity.Users;
import com.fileManager.service.IUsersService;
import com.fileManager.util.JwtUtil;
import com.fileManager.vo.LoginRequest;
import com.fileManager.vo.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final IUsersService usersService;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, IUsersService usersService) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 验证用户名和密码（可从数据库中查询）
        Users userNow = usersService.getUserByUsername(loginRequest.getUsername());
        if(userNow!=null&&passwordEncoder.matches(loginRequest.getPassword(),userNow.getPassword())){
            // 生成 Token
            String token = jwtUtil.generateToken(loginRequest.getUsername());
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户信息
        boolean result = usersService.addUser(user);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }
}
