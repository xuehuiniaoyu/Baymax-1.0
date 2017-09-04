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
public @interface Tag_Return {
    /**
     * 不做处理
     */
    int DEFAULT = 0;

    /**
     * json解析返回对象
     */
    int JSON_FORMAT = 1;

    /**
     *
     * 重定向到 Activity/Service
     *
     */
    int REDIRECT = 2;

    /**
     * 返回值类型
     * @return
     */
    int type() default DEFAULT;

    Class<?> toClass() default Tag_Return.class;
}
