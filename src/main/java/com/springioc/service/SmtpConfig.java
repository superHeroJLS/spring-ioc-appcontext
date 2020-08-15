package com.springioc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 一种注入配置的方式是先通过一个简单的JavaBean持有所有的配置，例如，一个SmtpConfig。
 * 
 * 使用一个独立的JavaBean持有所有属性，然后在其他Bean中以#{bean.property}注入的好处是，多个Bean都可以引用同一个Bean的某个属性。
 * 例如，如果SmtpConfig决定从数据库中读取相关配置项，那么MailService注入的@Value("#{smtpConfig.host}")仍然可以不修改正常运行。
 * @author Administrator
 *
 */
@Component("smtpConfig")
public class SmtpConfig {
    // 将app.properties中的smtp.host注入到host中，在需要使用到host时候的使用@Value("#{smtpConfig.host}")注入(参见MailService)
    @Value("${smtp.host}")
    private String host;

    @Value("${smtp.port:25}")
    private int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
