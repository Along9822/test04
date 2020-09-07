package com.common.platform.auth.context;

import com.common.platform.base.config.context.SpringContextHolder;

/**
 * 当前登录用户信息获取的接口
 */
public class LoginContextHolder {

    public static LoginContext getContext() {
        return SpringContextHolder.getBean(LoginContext.class);
    }

}

