package com.disney4a.baymax_example;

import com.disney4a.baymax.core.app.application.BaymaxApplication;
import com.disney4a.baymax.core.app.application.Baymax;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

public class ExampleApplication extends BaymaxApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Baymax.single().setAnnotationsPackage("com.disney4a.baymax_example.app").play();
    }
}
