package com.levi.mapper.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.levi.model.BaseEntity;
import com.levi.model.enums.FileType;
import com.levi.model.view.FileView;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("t_file")
@AutoMapper(target = FileView.class, reverseConvertGenerate = false, useEnums = FileType.class)
public class FileEntity extends BaseEntity {

    @Schema(description = "文件/文件夹编号")
    @TableId(type = IdType.AUTO)
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

    /**
     * {@link FileType}
     */
    @Schema(description = "文件类型")
    @AutoMapping(target = "fileType")
    private Integer type;
}
