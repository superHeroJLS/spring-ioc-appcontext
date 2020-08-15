package com.springioc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@Profile("dev")
public class Validators {

    @Autowired(required = false) // 注入List，若没要找到要注入的Class，validators值为空ArrayList
    private List<Validator> validators = new ArrayList<>();
    
    public void validate(String email, String password, String name) {
        for (Validator validator : this.validators) {
            validator.validate(email, password, name);
        }
    }
    
}
