package com.common.platform.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.platform.sys.base.pojo.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 基础字典
 */

@TableName("sys_dict")
@Getter
@Setter
@ToString
public class Dict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    @TableId(value = "dict_id", type = IdType.ASSIGN_ID)
    private Long dictId;

    /**
     * 所属字典类型的id
     */
    @TableField("dict_type_id")
    private Long dictTypeId;

    /**
     * 字典编码
     */
    @TableField("code")
    private String code;

    /**
     * 字典名称
     */
    @TableField("name")
    private String name;

    /**
     * 上级代码id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 所有上级id
     */
    @TableField("parent_ids")
    private String parentIds;

    /**
     * 状态（字典）
     */
    @TableField("status")
    private String status;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 字典的描述
     */
    @TableField("description")
    private String description;

}

