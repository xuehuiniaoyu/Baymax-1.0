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
public @interface Tag_Controller {
    /**
     * 指向的服务器地址（根目录）
     *
     * @return
     */
    String uri() default "/";

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Path {
        /**
         * 别名
         * @return
         */
        String name();

        /**
         * 映射接口路径
         * 可以是相对路径，也可以是绝对路径。
         * @return
         */
        String value() default "";
    }
}
