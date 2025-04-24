package com.kunmi.taskManager.taskManager.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RetryOnFailure {
    int attempts() default 3;
    int delay() default  1000;
    Class<? extends Throwable> [] retryFor() default {Exception.class};
}
