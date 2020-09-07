package com.common.platform.sys.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.sys.modular.system.entity.Position;
import com.common.platform.sys.modular.system.model.params.PositionParam;
import com.common.platform.sys.modular.system.model.result.PositionResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 职位表 Mapper 接口
 */
public interface PositionMapper extends BaseMapper<Position> {

    /**
     * 获取map列表
     */
    List<Map<String, Object>> getAllPositionMap();

    /**
     * 获取分页实体列表
     */
    Page<PositionResult> customPageList(@Param("page") Page page, @Param("paramCondition") PositionParam paramCondition);

}

