package com.disney4a.baymax.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag_Service {
    /**
     * 别名，通过该名称可以找到对应的Service
     * @return
     */
    String name();
}
