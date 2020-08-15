package com.springioc.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("userService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserService {

    @Autowired
	private MailService mailService;

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	private List<User> users = Arrays.asList(new User[]{
	        new User(1, "bob@example.com", "password", "Bob"),
            new User(2, "alice@example.com", "password", "Alice"),
            new User(3, "tom@example.com", "password", "Tom")}); 

	public User login(String email, String password) {
		for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
				mailService.sendLoginMail(user);
				return user;
			}
		}
		throw new RuntimeException("login failed.");
	}

	public User getUser(long id) {
		return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow(() -> new RuntimeException("user doesn't exists"));
	}

	public User register(String email, String password, String name) {
		users.forEach((user) -> {
			if (user.getEmail().equalsIgnoreCase(email)) {
				throw new RuntimeException("email exist.");
			}
		});
		User user = new User(users.stream().mapToLong(u -> u.getId()).max().getAsLong(), email, password, name);
		users.add(user);
		mailService.sendRegistrationMail(user);
		return user;
	}
}
