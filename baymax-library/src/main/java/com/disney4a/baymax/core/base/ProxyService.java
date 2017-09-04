package com.disney4a.baymax.core.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class ProxyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ProxyService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ProxyService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
