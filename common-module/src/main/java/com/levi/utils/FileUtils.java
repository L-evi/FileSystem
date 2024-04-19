package com.levi.utils;

import cn.hutool.core.util.StrUtil;
import com.levi.model.constant.SystemConstant;

public class FileUtils {
    /**
     * 格式化路径
     *
     * @param path 路径
     * @return
     */
    public static String formatPath(String path) {
        if (StrUtil.isBlank(path)) {
            return SystemConstant.INCLINE;
        }
        // 判断最后一位是否有/，没有补上
        String formatPath = path;
        if (StrUtil.equals(String.valueOf(path.charAt(path.length() - SystemConstant.ONE_INT_VALUE)), SystemConstant.INCLINE)) {
            formatPath += SystemConstant.INCLINE;
        }
        // 判断第一位有没有/，没有补上
        if (StrUtil.equals(String.valueOf(path.charAt(SystemConstant.ZERO_INT_VALUE)), SystemConstant.INCLINE)) {
            formatPath = SystemConstant.INCLINE + formatPath;
        }
        return formatPath;
    }
}
