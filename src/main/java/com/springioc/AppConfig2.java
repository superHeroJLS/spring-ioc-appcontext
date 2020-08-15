package com.springioc;

import java.time.ZoneId;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.springioc.service.SmtpMailService;

@Configuration
@ComponentScan
@PropertySource("app.properties")
public class AppConfig2 {
    
    // 如果当前的profile设置为非test，则Spring容器会调用createZoneIdForTest()创建ZoneId(实例化)
    // 运行程序时，加上JVM参数-Dspring.profiles.active=test就可以指定以test环境启动
    @Bean
    @Profile("!test")
    ZoneId createZoneId() {
        return ZoneId.systemDefault();
    }
    
    @Bean
    @Profile("test")
    ZoneId createZoneIdForTest() {
        return ZoneId.of("America/New_York");
    }
    
    @Bean
    @Profile({"test", "master"})
    ZoneId createZoneIdofMaster() {
        return ZoneId.of("z");
    }
    
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig2.class);
        SmtpMailService mailService = context.getBean(SmtpMailService.class);
        System.out.println("SmtpMailService: " + mailService);

    }
    
}
