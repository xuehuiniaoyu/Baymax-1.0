package com.disney4a.baymax.core.packaging;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.disney4a.baymax.core.app.application.Baymax;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class ServicePackaging {
    private String name;
    private Class<? extends Service> service;

    public ServicePackaging(String name, Class<? extends Service> service) {
        this.name = name;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Service> getService() {
        return service;
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
        cxt.startService(new Intent(context, service));
    }

    /**
     * 带参数启动
     * @param bundle
     */
    public void start(Bundle bundle) {
        Context context = Baymax.single().getContext();
        Intent intent = new Intent(context, service);
        intent.putExtras(bundle);
        Context cxt = null;
        try {
            cxt = context.createPackageContext(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        cxt.startService(intent);
    }
}
