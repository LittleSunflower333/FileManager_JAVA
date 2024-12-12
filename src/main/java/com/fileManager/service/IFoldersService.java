package com.fileManager.service;

import com.fileManager.entity.Folders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
public interface IFoldersService extends IService<Folders> {
    // 增加文件夹
    boolean addFolder(Folders folder);

    // 删除文件夹
    boolean deleteFolder(Long id);

    // 修改文件夹
    boolean updateFolder(Folders folder);
}
