package com.common.platform.config;

import com.common.platform.auth.context.LoginContextHolder;
import com.common.platform.base.config.database.AutoFullHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatisPlus的插件拓展和资源扫描
 */
@Configuration
@MapperScan(basePackages = {
        "com.common.platform.sys.modular.*.mapper"})
@EnableTransactionManagement(proxyTargetClass = true)
public class PluginsConfig {

    /**
     * 拓展核心包中的字段包装器
     */
    @Bean
    public AutoFullHandler mybatisPlusFieldHandler() {
        return new AutoFullHandler(){
            @Override
            protected Long getUserUniqueId() {
                try {
                    return LoginContextHolder.getContext().getUser().getId();
                } catch (Exception e) {
                    //如果获取不到当前用户就存空id
                    return -100L;
                }
            }
        };
    }
}