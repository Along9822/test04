package com.common.platform.auth.exception;

import com.common.platform.base.exception.AbstractBaseExceptionEnum;
import lombok.Data;

import static com.common.platform.auth.exception.enums.AuthExceptionEnum.NO_PERMISSION;

/**
 * 没有访问权限
 */
@Data
public class PermissionException extends RuntimeException {

    private Integer code;
    private String errorMessage;

    public PermissionException() {
        super(NO_PERMISSION.getMessage());
        this.code = NO_PERMISSION.getCode();
        this.errorMessage = NO_PERMISSION.getMessage();
    }

    public PermissionException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

}

