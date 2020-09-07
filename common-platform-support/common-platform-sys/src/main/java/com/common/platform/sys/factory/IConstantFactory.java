package com.common.platform.sys.factory;

import com.common.platform.sys.modular.system.entity.Dict;

import java.util.List;

/**
 * 常量生产工厂的接口
 */
public interface IConstantFactory {

    /**
     * 获取部门名称
     */
    String getDeptName(Long deptId);

    /**
     * 获取字典名称
     */
    String getDictName(Long dictId);

    /**
     * 根据字典名称和字典中的值获取对应的名称
     */
    String getDictsByName(String name, String code);

    /**
     * 获取字典名称
     */
    String getDictNameByCode(String dictCode);

    /**
     * 查询字典
     */
    List<Dict> findInDict(Long id);

    /**
     * 获取被缓存的对象(用户删除业务)
     */
    String getCacheObject(String para);

    /**
     * 获取子部门id
     */
    List<Long> getSubDeptId(Long deptId);

    /**
     * 获取所有父部门id
     */
    List<Long> getParentDeptIds(Long deptId);

    /**
     * 根据用户id获取用户名称
     */
    String getUserNameById(Long userId);

    /**
     * 根据用户id获取用户账号
     */
    String getUserAccountById(Long userId);

    /**
     * 根据角色id获取对应名称
     */
    String getSingleRoleName(Long roleId);

    /**
     * 根据角色id获取角色对应名称集合
     */
    String getRoleName(String roleIds);

    /**
     * 通过角色id获取对应的英文名称
     */
    String getSingleRoleTip(Long roleId);

    /**
     * 获取用户的性别
     */
    String getSexName(String sexCode);

    /**
     * 获取用户状态
     */
    String getStatusName(String status);

}

