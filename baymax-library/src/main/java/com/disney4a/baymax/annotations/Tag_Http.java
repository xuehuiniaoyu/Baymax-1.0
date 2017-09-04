package com.disney4a.baymax.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/8/18 0018.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag_Http {
    int GET = 0;
    int POST = 1;
    int PUT = 2;
    int DELETE = 3;

    int method() default GET;
}
