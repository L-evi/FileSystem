package com.levi.exception;

/**
 * 参数错误
 */
public class ParamException extends BusinessException {
    public ParamException(Integer code, String message) {
        super(code, message);
    }

    public ParamException(String message, Integer code) {
        super(message, code);
    }

    public ParamException(String message) {
        super(message);
    }

    public static ParamException paramMissError(String msg) {
        throw new ParamException("参数缺失: " + msg, 3001);
    }

    public static ParamException paramError(String msg) {
        throw new ParamException("参数错误: " + msg, 3002);
    }

    public static ParamException pageParamMissError() {
        return paramMissError("分页参数缺失");
    }
}
