package com.levi.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BucketConstant {

    BUCKET_ONE("common-file", "普通文件桶");

    private final String bucketName;

    private final String describe;

}
