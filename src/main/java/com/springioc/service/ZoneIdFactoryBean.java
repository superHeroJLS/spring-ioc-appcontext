package com.springioc.service;

import java.time.ZoneId;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 使用工厂模式创建bean
 * 定义了一个FactoryBean，要注意Spring创建的Bean实际上是这个FactoryBean的getObject()方法返回的Bean。
 * 为了和普通Bean区分，我们通常都以XxxFactoryBean命名。
 * @author Administrator
 *
 */
@Component
public class ZoneIdFactoryBean implements FactoryBean<ZoneId> {
    
    String zone = "Z";
    
    @Override
    public ZoneId getObject() throws Exception {
        System.out.println("beanFactor getObject()");
        return ZoneId.of(zone);
    }

    @Override
    public Class<?> getObjectType() {
        return ZoneId.class;
    }
    
}
