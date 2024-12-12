package com.fileManager.service.impl;

import com.fileManager.entity.Folders;
import com.fileManager.mapper.FoldersMapper;
import com.fileManager.service.IFoldersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class FoldersServiceImpl extends ServiceImpl<FoldersMapper, Folders> implements IFoldersService {
    private final FoldersMapper foldersMapper;

    public FoldersServiceImpl(FoldersMapper foldersMapper) {
        this.foldersMapper = foldersMapper;
    }

    // 增加文件夹
    @Override
    public boolean addFolder(Folders folder) {
        return this.save(folder); // MyBatis-Plus 提供的 save 方法
    }

    // 删除文件夹
    @Override
    public boolean deleteFolder(Long id) {
        return this.removeById(id); // MyBatis-Plus 提供的 removeById 方法
    }

    // 修改文件夹
    @Override
    public boolean updateFolder(Folders folder) {
        return this.updateById(folder); // MyBatis-Plus 提供的 updateById 方法
    }
}
