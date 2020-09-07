package com.common.platform.base.config.database;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自定义sql字段填充器
 */
public class AutoFullHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //判断是否存在指定字段
        if(metaObject.hasGetter(getCreateTimeFieldName())){
            //获取当前值
            Object createTime = getFieldValByName(getCreateTimeFieldName(), metaObject);
            if (createTime == null) {
                setFieldValByName(getCreateTimeFieldName(), new Date(), metaObject);
            }
        }
//判断是否存在指定字段
        if(metaObject.hasGetter(getCreateUserFieldName())){
//获取当前值
            Object createUser = getFieldValByName(getCreateUserFieldName(), metaObject);
            if (createUser == null) {
                //获取当前登录用户
                Object accountId = getUserUniqueId();
                setFieldValByName(getCreateUserFieldName(), accountId, metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //判断是否存在指定字段
        if(metaObject.hasGetter(getUpdateTimeFieldName())){
            setFieldValByName(getUpdateTimeFieldName(), new Date(), metaObject);
        }
        //判断是否存在指定字段
        if(metaObject.hasGetter(getUpdateUserFieldName())){
            //获取当前值
            Object updateUser = getFieldValByName(getUpdateUserFieldName(), metaObject);
            if (updateUser == null) {
                //获取当前登录用户
                Object accountId = getUserUniqueId();
                setFieldValByName(getUpdateUserFieldName(), accountId, metaObject);
            }
        }
    }
    /**
     * 获取创建时间字段的名称（非数据库中字段名称）
     */
    protected String getCreateTimeFieldName() {
        return "createTime";
    }

    /**
     * 获取创建用户字段的名称（非数据库中字段名称）
     */
    protected String getCreateUserFieldName() {
        return "createUser";
    }

    /**
     * 获取更新时间字段的名称（非数据库中字段名称）
     */
    protected String getUpdateTimeFieldName() {
        return "updateTime";
    }

    /**
     * 获取更新用户字段的名称（非数据库中字段名称）
     */
    protected String getUpdateUserFieldName() {
        return "updateUser";
    }

    /**
     * 获取用户唯一id（注意默认获取的用户唯一id为空，如果想填写则需要继承本类）
     */
    protected Object getUserUniqueId() {
        return "";
    }
}