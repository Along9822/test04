package com.common.platform.base.config.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailUtil {

    @Autowired
    Mail mail;

    public void send(String subject,String message) {
        if (mail.isFlag()) {
            HtmlEmail email = new HtmlEmail();
            try {
                email.setHostName(mail.getHost());
                email.setCharset("UTF-8");
                for (String str : mail.getReceivers()) {
                    email.addTo(str);
                }
                email.setFrom(mail.getFrom(), mail.getNickname());
                email.setSmtpPort(mail.getPort());
                email.setAuthentication(mail.getFrom(), mail.getPass());
                email.setSubject(subject);
                email.setMsg(message);
                email.send();
                log.info("{} 发送邮件到 {}", mail.getFrom(), StringUtils.join(mail.getReceivers(), ","));
            } catch (EmailException e) {
                log.error(mail.getFrom() + "发送邮件到" + StringUtils.join(mail.getReceivers(), ",") + "失败", e);
            }
        }
    }

}
