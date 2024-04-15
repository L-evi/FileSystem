package com.levi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.levi.mapper.file.FileEntity;
import com.levi.model.PageRequest;
import com.levi.model.PageView;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public interface FileService extends IService<FileEntity> {
    PageView<FileView> pageQuery(@NotNull PageRequest<FileRequest> fileRequestPageRequest);

    FileView detailByFileId(@NotNull Long fileId);


    Integer deleteByFileId(@NotEmpty Set<Long> fileIds);
}
