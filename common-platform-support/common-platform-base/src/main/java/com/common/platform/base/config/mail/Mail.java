package com.common.platform.base.config.mail;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "mail")
public class Mail {

    //接收邮件地址
    private Set<String> receivers;

    //SMTP发送服务器的名字
    private String host;

    //SMTP服务器端口
    private int port;

    //管理员邮箱地址
    private String from;

    //管理员邮箱密码
    private String pass;

    //管理员昵称
    private String nickname;

    //功能开启状态
    private boolean flag;

}
