package com.levi.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@AutoEnumMapper("type")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileType {

    FOLDER_TYPE(1, "文件夹"),
    FILE_TYPE(2, "文件");

    private final Integer type;

    private final String name;
}
