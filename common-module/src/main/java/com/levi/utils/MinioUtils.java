package com.levi.utils;



import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.levi.model.constant.SystemConstant;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Component
@Validated
public class MinioUtils {
    @Resource
    private MinioClient minioClient;

    /**
     * 上传文件
     *
     * @param bucketName  桶名
     * @param filePath    文件路径
     * @param filename    文件名
     * @param contentType 文件媒体类型
     * @param inputStream 输入流
     * @param streamSize  流长度
     * @return
     */
    public ObjectWriteResponse putObject(@NotBlank String bucketName,
                                         @NotBlank String filePath,
                                         @NotBlank String filename,
                                         @NotBlank String contentType,
                                         @NotNull InputStream inputStream,
                                         @NotNull Long streamSize) {
        filePath = FileUtils.formatPath(filePath);
        try {
            return minioClient.putObject(PutObjectArgs.builder()
                    .object(filePath + filename)
                    .contentType(contentType)
                    .bucket(bucketName)
                    .stream(inputStream, streamSize, -1)
                    .build());
        } catch (Exception e) {
            catchThrowException(e);
        }
        return null;
    }

    /**
     * 创建文件夹
     *
     * @param bucketName 桶名
     * @param folderName 文件夹名
     * @param path       文件夹前路径
     * @return
     */
    public ObjectWriteResponse createFolder(@NotBlank String bucketName,
                                            @NotBlank String folderName,
                                            @NotBlank String path) {
        path = FileUtils.formatPath(path);
        // 判断第一位是否有/，有要删除
        if (StrUtil.equals(String.valueOf(folderName.charAt(0)), SystemConstant.INCLINE)) {
            folderName = StrUtil.sub(folderName, 1, folderName.length());
        }
        // 判断最后一位是否有/，没有补上
        if (StrUtil.equals(String.valueOf(folderName.charAt(folderName.length() - 1)), SystemConstant.INCLINE)) {
            folderName += SystemConstant.INCLINE;
        }
        try {
            return minioClient.putObject(PutObjectArgs.builder()
                    .object(path + folderName)
                    .bucket(bucketName)
                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                    .build());
        } catch (Exception e) {
            catchThrowException(e);
        }
        return null;
    }

    private void catchThrowException(Exception e) {
        log.warn("minio client 发生错误");
        throw new RuntimeException(e);
    }
}
