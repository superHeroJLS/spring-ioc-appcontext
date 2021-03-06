package com.springioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.springioc.service.User;
import com.springioc.service.UserService;

/**
 * 读取在application.xml文件中配置的JavaBean（包括需要实例化和注入的JavaBean）
 * @author Administrator
 *
 */
public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
		UserService userService = context.getBean(UserService.class);
		User user = userService.login("bob@example.com", "password");
		System.out.println();
		System.out.println(user.getName());
	}
}
