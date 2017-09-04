package com.disney4a.baymax.core.app.activity;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.disney4a.baymax.utils.ViewSelector;

/**
 * Created by tjy on 2017/8/15 0015.
 * 用过注解以后你就知道为什么我们很讨厌写findViewById(R.id.xxx)了，
 * 因为它太烦人了！
 */

public class BaymaxActivity extends Activity {
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
