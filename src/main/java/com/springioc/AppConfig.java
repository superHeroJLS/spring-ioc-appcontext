package com.springioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.springioc.service.User;
import com.springioc.service.UserService;

/**
 * 读取使用Annotation配置的JavaBean（包括需要实例化和注入的JavaBean）
 * @author jls
 */
@Configuration
@ComponentScan({"test","com.itranswarp.learnjava"})
public class AppConfig {
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());
        
//        System.out.println(context.getBean(Test.class));
	}
	
}
