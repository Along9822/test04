package com.common.platform.base.config.database;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement)metaStatementHandler.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType()))
            return invocation.proceed();
        Connection connection = (Connection)invocation.getArgs()[0];
        DbType dbType = JdbcUtils.getDbType(connection.getMetaData().getURL());
        BoundSql boundSql = (BoundSql)metaStatementHandler.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();
        DataScope dataScope = findDataScopeObject(parameterObject);
        if (dataScope == null)
            return invocation.proceed();
        String scopeName = dataScope.getScopeName();
        List<Long> deptIds = dataScope.getDeptIds();
        String join = CollectionUtil.join(deptIds, ",");
        if (dbType != null && dbType.equals(DbType.POSTGRE_SQL)) {
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope.\"" + scopeName + "\" in (" + join + ")";
        } else {
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
        }
        metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
        return invocation.proceed();
    }

    public DataScope findDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof DataScope)
            return (DataScope)parameterObj;
        if (parameterObj instanceof Map)
            for (Object val : ((Map)parameterObj).values()) {
                if (val instanceof DataScope)
                    return (DataScope)val;
            }
        return null;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {}
}

