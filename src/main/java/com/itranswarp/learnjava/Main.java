package com.itranswarp.learnjava;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.itranswarp.learnjava.service.User;
import com.itranswarp.learnjava.service.UserService;

/**
 * 读取在application.xml文件中配置的JavaBean（包括需要实例化和注入的JavaBean）
 * @author jls
 *
 */
public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
		UserService userService = context.getBean(UserService.class);
		User user = userService.login("bob@example.com", "password");
		System.out.println(user.getName());
		
		System.out.println(userService.getUser(3));
	}
}
