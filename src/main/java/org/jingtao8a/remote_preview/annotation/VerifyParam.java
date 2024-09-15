package org.jingtao8a.remote_preview.annotation;

import org.jingtao8a.remote_preview.enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface VerifyParam {
    int min() default -1;
    int max() default -1;
    boolean required() default false;
    VerifyRegexEnum regex() default VerifyRegexEnum.NO;
}

