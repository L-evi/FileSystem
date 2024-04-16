package com.levi.model.request;

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
public class FileRequest {
    @Schema(description = "文件编号")
    private String fileId;

    @Schema(description = "文件名")
    private String filename;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件媒体类型")
    private String contentType;

    @Schema(description = "文件夹编号")
    private String folderId;

    @Schema(description = "绝对路径")
    private String absolutePath;

    @Schema(description = "桶名")
    private String bucketName;

    @Schema(description = "文件类型")
    private Integer type;

    @Schema(description = "文件上传类型")
    private MultipartFile[] files;
}
