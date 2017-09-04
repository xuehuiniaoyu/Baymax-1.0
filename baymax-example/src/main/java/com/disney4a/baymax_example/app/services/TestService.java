package com.disney4a.baymax_example.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.disney4a.baymax.annotations.Tag_Service;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

@Tag_Service(name = "test-service")
public class TestService extends Service {
    public static final String TAG = TestService.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand: " +intent.getStringExtra("hello"), Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }
}
