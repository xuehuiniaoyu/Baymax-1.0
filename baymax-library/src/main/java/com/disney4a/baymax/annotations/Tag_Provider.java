package com.disney4a.baymax.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag_Provider {
    /**
     * 别名，通过该名称可以找到对应的ContentProvider
     * @return
     */
    String name();
}
