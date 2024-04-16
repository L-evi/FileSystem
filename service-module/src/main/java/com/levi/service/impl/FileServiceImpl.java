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
import com.levi.model.constant.SystemConstant;
import com.levi.model.enums.FileType;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import com.levi.service.FileService;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class FileServiceImpl extends MPJBaseServiceImpl<FileMapper, FileEntity> implements FileService {

    @Resource
    private FileConverter fileConverter;

    @Resource
    private MinioClient minioClient;

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

    @Override
    public List<FileView> batchUpload(FileRequest fileRequest) {
        if (Objects.isNull(fileRequest.getFiles()) || fileRequest.getFiles().length == SystemConstant.ZERO_VALUE) {
            return Lists.newArrayList();
        }
        List<MultipartFile> files = Arrays.asList(fileRequest.getFiles());
        List<FileEntity> fileEntities = new ArrayList<>();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<FileEntity>> futures = files.stream()
                    .map(file -> executor.submit(() -> {
                        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                                .object(file.getOriginalFilename())
                                .contentType(file.getContentType())
                                .bucket(fileRequest.getBucketName())
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .build());
                        if (Objects.isNull(objectWriteResponse) || StrUtil.isBlank(objectWriteResponse.object())) {
                            log.info("文件上传失败 文件名 {} 文件路径 {} 桶名 {}", file.getOriginalFilename(), fileRequest.getAbsolutePath(), fileRequest.getBucketName());
                            throw new IOException("文件上传失败");
                        }
                        FileEntity fileEntity = FileEntity.builder()
                                .fileSize(file.getSize())
                                .type(FileType.FILE_TYPE.getType())
                                .absolutePath(fileRequest.getAbsolutePath())
                                .bucketName(fileRequest.getBucketName())
                                // todo 补充参数 另外还要完成文件夹的创建工作 需要分割绝对路径 检查各个文件夹是否存在
                                .build();
                        return fileEntity;
                    }))
                    .toList();
            // todo 对运行结果进行收集 并存储到数据库中
        } catch (Exception e) {
            log.error("发生错误", e);
            return Lists.newArrayList();
        }
        files.forEach(file -> {
            Thread.startVirtualThread(() -> {

            });
        });
        return List.of();
    }
}
