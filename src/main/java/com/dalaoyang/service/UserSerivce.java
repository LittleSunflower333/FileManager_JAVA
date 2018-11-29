package com.dalaoyang.service;


import com.baomidou.mybatisplus.service.IService;
import com.dalaoyang.entity.User;

import java.util.List;

public interface UserSerivce extends IService<User> {
    List<User> getUserList(Long id);
}
