package com.levi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.levi.converter.FileConverter;
import com.levi.mapper.file.FileEntity;
import com.levi.mapper.file.FileMapper;
import com.levi.model.PageRequest;
import com.levi.model.PageView;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import com.levi.service.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class FileServiceImpl extends MPJBaseServiceImpl<FileMapper, FileEntity> implements FileService {

    @Resource
    private FileConverter fileConverter;

    @Override
    public PageView<FileView> pageQuery(PageRequest<FileRequest> fileRequestPageRequest) {
        FileRequest entity = fileRequestPageRequest.getEntity();
        MPJLambdaWrapper<FileEntity> wrapper = MPJWrappers.<FileEntity>lambdaJoin();
        if (Objects.nonNull(entity)) {
            wrapper.eq(Objects.nonNull(entity.getFileId()), FileEntity::getFileId, entity.getFileId())
                    .like(StrUtil.isNotBlank(entity.getFilename()), FileEntity::getFilename, entity.getFilename())
                    .eq(StrUtil.isNotBlank(entity.getContentType()), FileEntity::getContentType, entity.getContentType())
                    .eq(Objects.nonNull(entity.getFolderId()), FileEntity::getFolderId, entity.getFolderId())
                    .eq(StrUtil.isNotBlank(entity.getBucketName()), FileEntity::getBucketName, entity.getBucketName())
                    .eq(Objects.nonNull(entity.getType()), FileEntity::getType, entity.getType());
        }
        Page<FileEntity> pageResult = baseMapper.selectPage(fileRequestPageRequest.ofPage(), wrapper);
        return new PageView<>(pageResult, fileConverter.entity2View(pageResult.getRecords()));
    }

    @Override
    public FileView detailByFileId(Long fileId) {
        FileEntity fileEntity = baseMapper.selectById(fileId);
        return fileConverter.entity2View(fileEntity);
    }

    @Override
    @Transactional
    public Integer deleteByFileId(Set<Long> fileIds) {
        return baseMapper.deleteBatchIds(fileIds);
    }
}
