package com.levi.model.request;

import com.levi.model.entity.FileEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@AutoMapper(target = FileEntity.class)
public class FileRequest {
    @Schema(description = "文件/文件夹编号")
    private Long fileFolderId;

    @Schema(description = "父文件夹编号")
    private Long parentFolderId;

    @Schema(description = "文件名")
    private String filename;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件媒体类型")
    private String contentType;


    @Schema(description = "绝对路径")
    private String absolutePath;

    @Schema(description = "桶名")
    private String bucketName;

    @Schema(description = "文件类型")
    private Integer type;

    @Schema(description = "文件上传类型")
    private MultipartFile[] files;
}
