package com.disney4a.baymax.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/8/15 0015.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag_Activity {
    /**
     * 别名，通过该名称可以找到对应的Activity
     * @return
     */
    String name();
}
