package com.common.platform.sys.exception;

import com.common.platform.base.exception.ServiceException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestEmptyException extends ServiceException {

    public RequestEmptyException() {
        super(400, "请求数据不完整或格式错误！");
    }

    public RequestEmptyException(String errorMessage) {
        super(400, errorMessage);
    }

    /**
     * 不拷贝栈信息，提高性能
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}

