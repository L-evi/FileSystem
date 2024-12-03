
package com.levi.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.levi.mapper.FileMapper;
import com.levi.model.PageRequest;
import com.levi.model.PageView;
import com.levi.model.constant.BucketConstant;
import com.levi.model.constant.SystemConstant;
import com.levi.model.entity.FileEntity;
import com.levi.model.enums.FileType;
import com.levi.model.request.FileRequest;
import com.levi.model.view.FileView;
import com.levi.service.FileService;
import com.levi.utils.FileUtils;
import com.levi.utils.MinioUtils;
import io.github.linpeilie.Converter;
import io.minio.ObjectWriteResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class FileServiceImpl extends MPJBaseServiceImpl<FileMapper, FileEntity> implements FileService {

    @Resource
    @Lazy
    private FileService fileService;

    @Resource
    private MinioUtils minioUtils;

    @Override
    public PageView<FileView> pageQuery(PageRequest<FileRequest> fileRequestPageRequest) {
        FileRequest entity = fileRequestPageRequest.getEntity();
        MPJLambdaWrapper<FileEntity> wrapper = MPJWrappers.<FileEntity>lambdaJoin();
        if (Objects.nonNull(entity)) {
            wrapper.eq(Objects.nonNull(entity.getFileFolderId()), FileEntity::getFileFolderId, entity.getFileFolderId())
                    .like(StrUtil.isNotBlank(entity.getFilename()), FileEntity::getFilename, entity.getFilename())
                    .eq(StrUtil.isNotBlank(entity.getContentType()), FileEntity::getContentType, entity.getContentType())
                    .eq(Objects.nonNull(entity.getParentFolderId()), FileEntity::getParentFolderId, entity.getParentFolderId())
                    .eq(StrUtil.isNotBlank(entity.getBucketName()), FileEntity::getBucketName, entity.getBucketName())
                    .eq(Objects.nonNull(entity.getType()), FileEntity::getType, entity.getType());
        }
        Page<FileEntity> pageResult = baseMapper.selectPage(fileRequestPageRequest.ofPage(), wrapper);
        return new PageView<>(pageResult, converter.convert(pageResult.getRecords(), FileView.class));
    }

    @Resource
    private Converter converter;

    @Override
    @Transactional
    public Integer deleteByFileId(Set<Long> fileIds) {
        return baseMapper.deleteBatchIds(fileIds);
    }

    @Override
    @Transactional
    public List<FileView> batchUpload(FileRequest fileRequest) {
        if (Objects.isNull(fileRequest.getFiles()) || fileRequest.getFiles().length == SystemConstant.ZERO_VALUE) {
            return Lists.newArrayList();
        }
        List<MultipartFile> files = Arrays.asList(fileRequest.getFiles());
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<FileEntity>> futures = files.stream()
                    .map(file -> executor.submit(() -> {
                        ObjectWriteResponse objectWriteResponse = minioUtils.putObject(fileRequest.getBucketName(), fileRequest.getAbsolutePath(), file.getOriginalFilename(), file.getContentType(), file.getInputStream(), file.getSize());
                        if (Objects.isNull(objectWriteResponse) || StrUtil.isBlank(objectWriteResponse.object())) {
                            log.info("文件上传失败 文件名 {} 文件路径 {} 桶名 {}", file.getOriginalFilename(), fileRequest.getAbsolutePath(), fileRequest.getBucketName());
                            throw new IOException("文件上传失败");
                        }
                        FileEntity fileEntity = FileEntity.builder()
                                .fileSize(file.getSize())
                                .type(FileType.FILE_TYPE.getType())
                                .absolutePath(fileRequest.getAbsolutePath())
                                .bucketName(fileRequest.getBucketName())
                                .build();
                        // todo 补充参数 另外还要完成文件夹的创建工作 需要分割绝对路径 检查各个文件夹是否存在
                        return fileEntity;
                    }))
                    .toList();
            // todo 对运行结果进行收集 并存储到数据库中
            int count = futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            log.error("获取运行结果失败");
                            throw new RuntimeException(e);
                        }
                    })
                    .map(fileEntity -> baseMapper.insert(fileEntity))
                    .mapToInt(Integer::intValue)
                    .sum();
            if (count != files.size()) {
                log.error("保存数据失败");
                return Lists.newArrayList();
            }
        } catch (Exception e) {
            log.error("发生错误", e);
            return Lists.newArrayList();
        }
        return List.of();
    }

    public List<FileView> recursiveCreateFolders(FileRequest fileRequest) {
        // 参数校验
        if (StrUtil.isBlank(fileRequest.getAbsolutePath()) || StrUtil.isBlank(fileRequest.getBucketName())) {
            log.warn("文件路径 {} 或 桶名 {} 缺失", fileRequest.getAbsolutePath(), fileRequest.getBucketName());
            return Lists.newArrayList();
        }
        // 根据父文件夹是否存在进行不同的逻辑
        Long parentFolderId = fileRequest.getParentFolderId();
        if (Objects.isNull(parentFolderId)) {
            // todo 补充创建文件夹方法
            fileRequest.setAbsolutePath(FileUtils.formatPath(fileRequest.getAbsolutePath()));
            ObjectWriteResponse response = minioUtils.createFolder(fileRequest.getBucketName(), fileRequest.getFilename(), fileRequest.getAbsolutePath());
            if (Objects.isNull(response)) {
                return Lists.newArrayList();
            }
            // 切割路径存储到数据库
            String path = fileRequest.getAbsolutePath();
            List<String> paths = StrUtil.split(path, SystemConstant.INCLINE);
            if (CollUtil.isEmpty(paths)) {

            }
        } else {
            // 检查父文件夹是否存在
            FileView parentFolderView = detailByFileFolderId(parentFolderId);
            if (Objects.isNull(parentFolderView)) {
                log.warn("父文件夹不存在 parentFolderId {}", parentFolderId);
                return Lists.newArrayList();
            }
            // todo 补充创建文件夹逻辑<a href="https://blog.csdn.net/weixin_42170236/article/details/107176495?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2-107176495-blog-128471616.235%5Ev43%5Epc_blog_bottom_relevance_base3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2-107176495-blog-128471616.235%5Ev43%5Epc_blog_bottom_relevance_base3&utm_relevant_index=5"></a>        }
            return Lists.newArrayList();
        }
        return List.of();
    }

    private List<FileView> recursiveCreateFolders(FileEntity parentFolderEntity, String folderName, String bucketName) {
        if (Objects.isNull(parentFolderEntity) || StrUtil.isBlank(folderName) || StrUtil.isBlank(bucketName)) {
            log.warn("父文件夹对象 {} 文件夹名称 {} 桶名 {} 之一不能为空", parentFolderEntity, folderName, bucketName);
            return Lists.newArrayList();
        }
        return null;
    }

    @Override
    public FileView detailByFileFolderId(Long fileFolderId) {
        FileEntity fileEntity = baseMapper.selectById(fileFolderId);
        return converter.convert(fileEntity, FileView.class);
    }

    @Override
    public FileView fileUpload(FileRequest fileRequest) {
        if (Objects.isNull(fileRequest) || fileRequest.getFiles().length == 0) {
            return null;
        }
        MultipartFile file = fileRequest.getFiles()[0];
        String filename = fileRequest.getFilename();
        Long fileSize = fileRequest.getFileSize();
        String contentType = fileRequest.getContentType();
        String absolutePath = FileUtils.formatPath(fileRequest.getAbsolutePath());
        String bucketName = fileRequest.getBucketName();
        Integer type = Objects.nonNull(fileRequest.getType()) ? fileRequest.getType() : 1;
        if (StrUtil.isBlank(filename)) {
            filename = file.getOriginalFilename();
        }
        if (Objects.isNull(fileSize)) {
            fileSize = file.getSize();
        }
        if (StrUtil.isBlank(contentType)) {
            contentType = file.getContentType();
        }
        if (StrUtil.isBlank(bucketName)) {
            bucketName = BucketConstant.BUCKET_ONE.getBucketName();
        }
        // minio上传
        try {
            ObjectWriteResponse objectWriteResponse = minioUtils.putObject(bucketName, absolutePath, filename, contentType, file.getInputStream(), fileSize);
            if (Objects.isNull(objectWriteResponse)) {
                throw new RuntimeException("上传文件到minio失败");
            }
            // 保存到数据库
            FileEntity fileEntity = FileEntity.builder()
                    .parentFolderId(Objects.nonNull(fileRequest.getParentFolderId()) ? fileRequest.getParentFolderId() : -1)
                    .filename(filename)
                    .fileSize(fileSize)
                    .contentType(contentType)
                    .absolutePath(absolutePath)
                    .bucketName(bucketName)
                    .type(type)
                    .build();
            int insert = baseMapper.insert(fileEntity);
            if (insert < 1) {
                throw new RuntimeException("插入文件数据到数据库失败");
            }
            return detailByFileFolderId(fileEntity.getFileFolderId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}