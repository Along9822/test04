package com.common.platform.sys.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.sys.modular.system.entity.UserPos;
import com.common.platform.sys.modular.system.model.params.UserPosParam;
import com.common.platform.sys.modular.system.model.result.UserPosResult;
import org.apache.ibatis.annotations.Param;

/**
 * 用户职位关联表 Mapper 接口
 */
public interface UserPosMapper extends BaseMapper<UserPos> {

    /**
     * 获取分页实体列表
     */
    Page<UserPosResult> customPageList(@Param("page") Page page, @Param("paramCondition") UserPosParam paramCondition);

}