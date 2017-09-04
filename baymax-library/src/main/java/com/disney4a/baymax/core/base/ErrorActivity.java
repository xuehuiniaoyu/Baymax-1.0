package com.disney4a.baymax.core.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;

import com.disney4a.baymax.annotations.Tag_Activity;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
@Tag_Activity(name = "error")
public class ErrorActivity extends android.app.Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("error");
        textView.setGravity(Gravity.CENTER);
        setContentView(textView);
    }
}
