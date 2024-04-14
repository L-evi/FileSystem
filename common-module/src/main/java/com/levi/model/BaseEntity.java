package com.levi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @Schema(description = "添加时间")
    private LocalTime createTime;

    @Schema(description = "修改时间")
    private LocalTime updateTime;

    @Schema(description = "是否删除：0-未删除，1-删除")
    private Integer deleted;
}
