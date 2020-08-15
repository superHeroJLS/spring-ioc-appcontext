package com.springioc;

import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.springioc.service.User;
import com.springioc.service.UserService;
import com.springioc.service.Validators;

/**
 * 读取使用Annotation配置的JavaBean（包括需要实例化和注入的JavaBean）
 * @author Administrator
 *
 */
@Configuration
@ComponentScan // 默认basePackage: AppConfig所在包和子包
public class AppConfig {
    
    /*
     * 有些时候，我们需要对一种类型的Bean创建多个实例。例如，同时连接多个数据库，就必须创建多个DataSource实例。
     * 如果我们在@Configuration类中创建了多个同类型的Bean：
     */
    @Bean("z")
    @Primary    // 指定为主要的Bean，也是默认被注入的Bean，这种方式也很常用。例如，对于主从两个数据源，通常将主数据源定义为@Primary。
    ZoneId createZoneOfZ() {
        return ZoneId.of("Z");
    }

    @Bean
    @Qualifier("utc8")
    ZoneId createZoneOfUTC8() {
        return ZoneId.of("UTC+08:00");
    }
    
    public static void main(String[] args) {
        
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());
        
        Validators vs = context.getBean(Validators.class);
        vs.validate("bob@example.com", "password", "bob");
    
    }
}
