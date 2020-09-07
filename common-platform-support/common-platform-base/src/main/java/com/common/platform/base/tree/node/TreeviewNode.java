package com.common.platform.base.tree.node;

import com.common.platform.base.tree.Tree;
import lombok.Data;

import java.util.List;

/**
 * 部门的节点
 */
@Data
public class TreeviewNode implements Tree {

    /**
     * 附加信息，一般用于存业务id
     */
    private String tags;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 节点名称
     */
    private String text;

    /**
     * 子节点
     */
    private List<TreeviewNode> nodes;

    @Override
    public String getNodeId() {
        return tags;
    }

    @Override
    public String getNodeParentId() {
        return parentId;
    }

    @Override
    public void setChildrenNodes(List childrenNodes) {
        this.nodes = childrenNodes;
    }
}

