package com.disney4a.baymax_example.app.activity.baymax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.disney4a.baymax.annotations.Tag_Activity;
import com.disney4a.baymax.core.app.activity.BaymaxCompatActivity;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

@Tag_Activity(name = "test-activity")
public class TestActivity extends BaymaxCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("测试Activity");
        setContentView(textView);
    }
}
