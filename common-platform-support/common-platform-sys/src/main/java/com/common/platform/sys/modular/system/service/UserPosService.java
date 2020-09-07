package com.common.platform.sys.modular.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.platform.base.page.LayuiPageInfo;
import com.common.platform.sys.modular.system.entity.UserPos;
import com.common.platform.sys.modular.system.model.params.UserPosParam;

/**
 * 用户职位关联表 服务类
 */
public interface UserPosService extends IService<UserPos> {

    /**
     * 新增
     */
    void add(UserPosParam param);

    /**
     * 删除
     */
    void delete(UserPosParam param);

    /**
     * 更新
     */
    void update(UserPosParam param);

    /**
     * 查询分页数据
     */
    LayuiPageInfo findPageBySpec(UserPosParam param);

}

