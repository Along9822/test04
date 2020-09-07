package com.common.platform.sys.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.platform.base.tree.node.ZTreeNode;
import com.common.platform.sys.modular.system.entity.DictType;

import java.util.List;

/**
 * 字典类型表 Mapper 接口
 */
public interface DictTypeMapper extends BaseMapper<DictType> {

    List<ZTreeNode> dictTree(Long dictTypeId);
}

