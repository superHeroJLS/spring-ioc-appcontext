package com.springaop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springioc.service.User;

@Component
public class UserService {

    @Autowired
    private MailService mailService;
    
    public void test() {
        System.out.println("welcome test!");
    }
    
    public void send() {
        mailService.sendLoginMail(new User(1,  null, null, "lisi"));
    }
    
    
}
