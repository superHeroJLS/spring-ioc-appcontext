package com.springioc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AppService {
    
    // 读取配置文件、资源文件等。使用Spring容器时，我们也可以把“文件”注入进来，方便程序读取，使用org.springframework.core.io.Resource
    @Value("classpath:/logo.txt")
    private Resource resource;
    
    @Value("classpath:/app.properties")
    private Resource resourceP;
    
    public String logo;
    public String prop;
    
    @PostConstruct
    public void init() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            this.logo = reader.lines().collect(Collectors.joining("\n"));
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceP.getInputStream(), StandardCharsets.UTF_8))) {
            this.prop = reader.lines().collect(Collectors.joining("\n"));
        }
    }
    
}
