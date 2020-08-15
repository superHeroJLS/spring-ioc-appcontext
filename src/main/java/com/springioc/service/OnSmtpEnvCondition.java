package com.springioc.service;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnSmtpEnvCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // OnSmtpEnvCondition的条件是存在JVM参数smtp，值为true。这样，我们就可以通过JVM参数来控制是否创建SmtpMailService
        return "true".equalsIgnoreCase(System.getProperty("smtp"));
    }

}
