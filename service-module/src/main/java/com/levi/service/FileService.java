package com.levi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.levi.model.PageRequest;
import com.levi.model.PageView;
import com.levi.model.entity.FileEntity;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public interface FileService extends IService<FileEntity> {
    PageView<FileView> pageQuery(@NotNull PageRequest<FileRequest> fileRequestPageRequest);

    Integer deleteByFileId(@NotEmpty Set<Long> fileIds);

    List<FileView> batchUpload(@NotNull FileRequest fileRequest);

    /**
     * 递归添加文件夹
     * <p>根据绝对路径分割并创建文件夹</p>
     * <p>如果parentId存在，那么就是在指定文件夹中创建文件夹</p>
     * <p>如果parentId不存在，那么就是在桶中直接创建文件夹</p>
     *
     * @param fileRequest 文件参数
     * @return 递归创建的所有文件夹
     */
    List<FileView> recursiveCreateFolders(@NotNull FileRequest fileRequest);

    FileView detailByFileFolderId(@NotNull Long fileFolderId);

    /**
     * 单个文件上传
     *
     * @param fileRequest
     * @return
     */
    FileView fileUpload(FileRequest fileRequest);
}