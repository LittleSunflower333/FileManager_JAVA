package com.fileManager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fileManager.entity.Files;
import com.fileManager.entity.Folders;
import com.fileManager.mapper.FilesMapper;
import com.fileManager.mapper.FoldersMapper;
import com.fileManager.service.IFoldersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fileManager.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private FoldersMapper foldersMapper;
    @Autowired
    private FilesMapper filesMapper;

    @Override
    public boolean addFolder(Folders folder,String userId) {
        if(userId==null){
            userId = UserContext.getCurrentUser();
        }
        folder.setCreatedBy(userId);
        return this.save(folder);
    }

    /**
     * 删除文件夹及其子文件夹和文件
     * @param folderId 要删除的文件夹 ID
     * @return 删除是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFolder(String folderId) {
        // 获取目标文件夹及其所有子内容
        Folders folder = foldersMapper.selectById(folderId);
        if (folder == null) {
            throw new IllegalArgumentException("文件夹不存在");
        }

        // 递归获取所有子文件夹 ID
        List<String> allFolderIds = new ArrayList<>();
        collectFolderIds(folderId, allFolderIds);

        // 获取所有文件 ID
        List<String> allFileIds = filesMapper.selectList(new LambdaQueryWrapper<Files>()
                .in(Files::getFolderId, allFolderIds)
        ).stream().map(Files::getId).collect(Collectors.toList());

        // 删除所有文件
        if (!allFileIds.isEmpty()) {
            filesMapper.deleteBatchIds(allFileIds);
        }

        // 删除所有文件夹
        if (!allFolderIds.isEmpty()) {
            foldersMapper.deleteBatchIds(allFolderIds);
        }

        return true;
    }

    /**
     * 递归收集文件夹 ID
     * @param folderId 当前文件夹 ID
     * @param folderIds 收集的文件夹 ID 列表
     */
    private void collectFolderIds(String folderId, List<String> folderIds) {
        folderIds.add(folderId); // 添加当前文件夹 ID

        // 查询子文件夹
        List<String> childFolderIds = foldersMapper.selectList(new LambdaQueryWrapper<Folders>()
                .eq(Folders::getParentFolderId, folderId)
        ).stream().map(Folders::getId).collect(Collectors.toList());

        // 递归收集子文件夹 ID
        for (String childFolderId : childFolderIds) {
            collectFolderIds(childFolderId, folderIds);
        }
    }
    // 修改文件夹
    @Override
    public boolean updateFolder(Folders folder) {
        return this.updateById(folder); // MyBatis-Plus 提供的 updateById 方法
    }
    /**
     * 根据创建者 ID 获取文件夹列表
     * @return 文件夹列表，包含子文件夹和文件信息
     */
    @Override
    public List<Folders> getFoldersTreeByUserId() {
        // 查询根文件夹（父文件夹 ID 为 null的文件夹）
        LambdaQueryWrapper<Folders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folders::getCreatedBy, UserContext.getCurrentUser())
                .isNull(Folders::getParentFolderId)
                .orderByAsc(Folders::getCreatedAt); // 按照 createdAt 升序排序;
        List<Folders> rootFolders = foldersMapper.selectList(queryWrapper);

        // 遍历根文件夹，递归查询子文件夹和文件
        for (Folders folder : rootFolders) {
            populateChildFoldersAndFiles(folder);
        }

        return rootFolders;
    }
    /**
     * 递归填充文件夹的子文件夹和文件信息
     * @param folder 当前文件夹
     */
    private void populateChildFoldersAndFiles(Folders folder) {
        // 查询子文件夹
        LambdaQueryWrapper<Folders> folderQuery = new LambdaQueryWrapper<>();
        folderQuery.eq(Folders::getParentFolderId, folder.getId());
        List<Folders> childFolders = foldersMapper.selectList(folderQuery);
        folder.setChildFolders(childFolders);

        // 查询子文件
        LambdaQueryWrapper<Files> fileQuery = new LambdaQueryWrapper<>();
        fileQuery.eq(Files::getFolderId, folder.getId());
        List<Files> childFiles = filesMapper.selectList(fileQuery);
        folder.setChildFiles(childFiles);

        // 递归查询每个子文件夹的子内容
        for (Folders childFolder : childFolders) {
            populateChildFoldersAndFiles(childFolder);
        }
    }
}
