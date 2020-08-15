package com.springioc.service;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;


/**
 * 根据@Conditional决定是否创建某个Bean;
 * Spring Boot提供了更多使用起来更简单的条件注解:@ConditionalOnProperty, @ConditionalOnClass
 * @author Administrator
 */
@Component
@Conditional(OnSmtpEnvCondition.class)
public class SmtpMailService {
    
}
