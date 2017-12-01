package com.disney4a.baymax.core.app.application;

import android.content.Context;

import com.disney4a.baymax.annotations.Tag_Application;
import com.disney4a.baymax.core.base.ProxyActivity;
import com.disney4a.baymax.core.base.ProxyService;

import org.litepal.LitePalApplication;

/**
 * Created by tjy on 2017/8/22 0022.
 * 应用程序的唯一入口；
 * 必须被第一个执行；
 *
 *      对一个程序的了解，通常都是从入口开始了解的，我希望在这里把这个框架的一些重要特性都阐述一遍。
 * 开发本框架的出发点是当我了解了Spring框架之后，智能的注解模式是的整个框架开发起来效率及其的高。
 * 所以大胆的设想了下，如果能在Android平台上开发这样一套框架，是不是对我们Android工程师来说也是一种好的开发工具。
 * 所以框架中使用到了一些注解：Tag_Activity、Tag_Service、Tag_Provider、Tag_Controller、Tag_Http、Tag_Return
 *
 * Tag_Activity、Tag_Service、Tag_Provider 是对Android三大组件做增强
 * Tag_Controller是新引入的概念，通过Tag_Controller我们可以把方法对应到uri上，这样就很好的构建了Rest
 * Tag_Http、Tag_Return是配合使用的，对网络做增强
 * Tag_Return标记了返回结果的类型，默认DEFAULT不做处理、REDIRECT处理重定向，处理完之后打开Activity/Service、JSON_FORMAT返回json对应的
 * 实体对象。
 *
 *      很抱歉，我们没有提供其他入口给开发者。这样做的好处是你不需要关心太多的Hook技术相关代码。
 * 从一个应用开发者的角度来考虑，我只关心使用框架提供的方法能做想做的事情就可以了，没有必要去关心其他额外的方法。
 * 我们尽量做到这一点，只公开一些必要方法。其他跟Hook有关的方法都尽量放在私有方法中去执行。
 * 当你想要开发Android程序的时候，首先切记一点，必须继承LittleApplication
 * 然后再AndroidManifest.xml的配置到入口。
 *
 * 然后你就可以使用BaymaxManager.single()对象了，这是一个全局变量。
 *
 * 网络相关
 * 我们使用了OKHttp做为引擎，并很好的结合到了Controller上；
 *
 * 数据库相关
 * 使用Android开源数据库框架litepal作为引擎，没有做任何二次开发，只将其引用到框架中。
 * 如果你希望使用litepal开发快速高效的数据库程序可以去github官网了解下litepal的详细使用规范。
 * 项目地址为：https://github.com/LitePalFramework/LitePal
 *
 * 想要更详细的了解本框架，请重点了解BaymaxManager类
 * @see Baymax
 */

public class BaymaxApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        // 先检查是否有注解
        Tag_Application application = this.getClass().getAnnotation(Tag_Application.class);
        if(application != null) {
            String[] annotationsPackages = application.annotationsPackages();
            Baymax.single().setAnnotationsPackage(annotationsPackages).play();
        }
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        Baymax.initialize(base).hook(ProxyActivity.class, ProxyService.class);
        super.attachBaseContext(base);
    }
}
