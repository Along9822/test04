package com.common.platform.base.exception;

public interface AbstractBaseExceptionEnum {
    /**
     * 获取异常的状态码
     */
    Integer getCode();

    /**
     * 获取异常的提示信息
     */
    String getMessage();
}

