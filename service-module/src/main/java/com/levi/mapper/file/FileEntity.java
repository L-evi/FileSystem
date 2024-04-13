package com.levi.mapper.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("t_file")
public class FileEntity {

    @Schema(description = "文件编号")
    @TableId(type = IdType.AUTO)
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
}
