package com.disney4a.baymax.core.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.ViewGroup;

import com.disney4a.baymax.utils.Reflect;
import com.disney4a.baymax.utils.ViewSelector;

/**
 * Created by tjy on 2017/8/24 0024.
 */

public class BaymaxCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Reflect reflect = Reflect.newInstance();
        // 解析出代理对象
        Intent realIntent = getIntent();
        Intent proxyIntent = realIntent.getParcelableExtra("bindIntent");
        if(proxyIntent != null) {
            reflect.on(this).set("mIntent", proxyIntent);
            realIntent.removeExtra("bindIntent");
            // 由于BaymaxCompatActivity在onCreate的时候做了一次验证，所以再一次Hook做欺骗
            AppCompatDelegate delegate = getDelegate();
            Object mContext = reflect.clear().on(delegate).get("mContext");
            reflect.clear().on(mContext).set("mIntent", proxyIntent);
            reflect.clear().on(mContext).set("mComponent", proxyIntent.getComponent());
            super.onCreate(savedInstanceState);
            // 还原Intent
            reflect.on(this).set("mIntent", realIntent);
            reflect.clear().on(mContext).set("mIntent", realIntent);
            reflect.clear().on(mContext).set("mComponent", realIntent.getComponent());
        }
        else {
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ViewSelector.setAnnotationsFrom(this).select(this).release();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ViewSelector.setAnnotationsFrom(this).select(this).release();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ViewSelector.setAnnotationsFrom(this).select(this).release();
    }
}
