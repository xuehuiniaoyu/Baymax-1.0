package com.disney4a.baymax_example;

import com.disney4a.baymax.annotations.Tag_Application;
import com.disney4a.baymax.core.app.application.BaymaxApplication;
import com.disney4a.baymax.core.app.application.Baymax;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

@Tag_Application(annotationsPackages = {"com.disney4a.baymax_example.app"})
public class ExampleApplication extends BaymaxApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        Baymax.single().setAnnotationsPackage("com.disney4a.baymax_example.app").play();
    }
}
