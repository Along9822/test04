package com.common.platform.sys.config;

import com.common.platform.base.config.context.SpringContextHolder;
import com.common.platform.sys.exception.page.ErrorView;
import com.common.platform.sys.listener.ConfigListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 定制HTTP消息转换器
 * */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 配置string解析器放在json解析器前边
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        HttpMessageConverter<?> httpMessageConverter = converters.get(0);
        converters.remove(0);
        converters.add(2, httpMessageConverter);
    }

    /**
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //本应用
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
    }

    /**
     * 默认错误页面，返回json
     */
    @Bean("error")
    public ErrorView error() {
        return new ErrorView();
    }

    /**
     * RequestContextListener注册
     */
    @Bean
    public ServletListenerRegistrationBean<RequestContextListener> requestContextListenerRegistration() {
        return new ServletListenerRegistrationBean<>(new RequestContextListener());
    }

    /**
     * ConfigListener注册
     */
    @Bean
    public ServletListenerRegistrationBean<ConfigListener> configListenerRegistration() {
        return new ServletListenerRegistrationBean<>(new ConfigListener());
    }



    @Bean
    public SpringContextHolder springContextHolder(){
        return new SpringContextHolder();
    }
}

