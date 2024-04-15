package com.levi.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    FOLDER_TYPE(1, "文件夹"),
    FILE_TYPE(2, "文件");

    Integer type;

    String name;
}
