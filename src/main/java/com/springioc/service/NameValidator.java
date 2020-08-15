package com.springioc.service;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)   // 当被注入到List中时，元素按照@Order排序
public class NameValidator implements Validator {
    public void validate(String email, String password, String name) {
        if (name == null || name.isEmpty() || name.length() > 20) {
            throw new IllegalArgumentException("invalid name: " + name);
        }
    }
}