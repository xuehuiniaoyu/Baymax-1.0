package com.disney4a.baymax.core.packaging;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.disney4a.baymax.core.app.application.Baymax;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class ActivityPackaging {
    private String name;
    private Class<? extends android.app.Activity> activity;

    public ActivityPackaging(String name, Class<? extends android.app.Activity> activity) {
        this.name = name;
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public Class<? extends android.app.Activity> getActivity() {
        return activity;
    }

    /**
     * 启动
     */
    public void start() {
        Context context = Baymax.single().getContext();
        Context cxt = null;
        try {
            cxt = context.createPackageContext(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cxt.startActivity(intent);
    }

    /**
     * 带参数启动
     * @param bundle
     */
    public void start(Bundle bundle) {
        Context context = Baymax.single().getContext();
        Intent intent = new Intent(context, activity);
        intent.putExtras(bundle);
        Context cxt = null;
        try {
            cxt = context.createPackageContext(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cxt.startActivity(intent);
    }
}
