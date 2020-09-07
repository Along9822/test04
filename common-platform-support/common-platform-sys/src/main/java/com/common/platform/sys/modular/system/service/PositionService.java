package com.common.platform.sys.modular.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.platform.base.page.LayuiPageInfo;
import com.common.platform.sys.modular.system.entity.Position;
import com.common.platform.sys.modular.system.model.params.PositionParam;

/**
 * 职位表 服务类
 */
public interface PositionService extends IService<Position> {

    /**
     * 新增
     */
    void add(PositionParam param);

    /**
     * 删除
     */
    void delete(PositionParam param);

    /**
     * 更新
     */
    void update(PositionParam param);

    /**
     * 查询分页数据，Specification模式
     */
    LayuiPageInfo findPageBySpec(PositionParam param);

    /**
     * 获取多选框的职位列表
     */
    LayuiPageInfo listPositions(Long userId);

}

