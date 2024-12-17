package com.fileManager.service.impl;

import com.fileManager.entity.Logs;
import com.fileManager.entity.Talk;
import com.fileManager.mapper.LogsMapper;
import com.fileManager.mapper.TalkMapper;
import com.fileManager.mapper.UsersMapper;
import com.fileManager.service.ILogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fileManager.service.ITalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements ITalkService {
    @Autowired
    private UsersMapper usersMapper;

}
