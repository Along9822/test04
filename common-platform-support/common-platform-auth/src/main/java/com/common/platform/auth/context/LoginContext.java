package com.common.platform.auth.context;

import com.common.platform.auth.pojo.LoginUser;

import java.util.List;

/**
 * 当前登录用户信息获取的接口
 */
public interface LoginContext {

    /**
     * 获取当前登录用户
     */
    LoginUser getUser();

    /**
     * 获取当前登录用户的token
     */
    String getToken();

    /**
     * 是否登录
     */
    boolean hasLogin();

    /**
     * 获取当前登录用户id
     */
    Long getUserId();

    /**
     * 验证当前用户是否包含该角色
     */
    boolean hasRole(String roleName);

    /**
     * 验证当前用户是否属于以下任意一个角色
     */
    boolean hasAnyRoles(String roleNames);

    /**
     * 验证当前用户是否拥有指定权限
     */
    boolean hasPermission(String permission);

    /**
     * 判断当前用户是否是超级管理员
     */
    boolean isAdmin();

    /**
     * 获取当前用户的部门数据范围的集合
     */
    List<Long> getDeptDataScope();

}

