package com.springioc.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("mailService")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MailService {
    
    // 注入beanFactory
    @Autowired
    private ZoneIdFactoryBean zoneIdFactory;
    
    @Autowired(required = false)
    @Qualifier("z") // 指定注入名称为 z 的ZoneId
	private ZoneId zoneId = ZoneId.systemDefault();
    
    @Value("#{smtpConfig.host}")
    public String smtpHost;

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public String getTime() {
		return ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	public void sendLoginMail(User user) {
		System.err.println(String.format("Hi, %s! You are logged in at %s", user.getName(), getTime()));
	}

	public void sendRegistrationMail(User user) {
		System.err.println(String.format("Welcome, %s!", user.getName()));

	}
	
	/*
	 * 在Bean的初始化和清理方法上标记@PostConstruct和@PreDestroy（JSR-250定义的Annotation）：
	 * Spring容器会对上述Bean做如下初始化流程：
	 * 1. 调用构造方法创建MailService实例；
	 * 2. 根据@Autowired进行注入；
	 * 3. 调用标记有@PostConstruct的init()方法进行初始化。
	 * 
	 * 而销毁时，容器会首先调用标记有@PreDestroy的shutdown()方法。
	 * Spring只根据Annotation查找无参数方法，对方法名不作要求
	 */
	@PostConstruct // 在Bean初始化之后被调用
    public void init() {
        System.out.println("Init mail service with zoneId = " + this.zoneId + " by @PostConstruct");
    }

    @PreDestroy // 在Bean销毁前被调用
    public void shutdown() {
        System.out.println("Shutdown mail service");
    }
    
    /**
     * 这是一个有xml文件中指定的init-method，这个方法的执行顺序在@PostConstruct之后
     */
    public void initMethod() {
        System.out.println("Init mail service with zoneId = " + this.zoneId + " by init-method");
    }
}
