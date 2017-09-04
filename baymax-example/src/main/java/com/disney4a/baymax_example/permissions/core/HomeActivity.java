package com.disney4a.baymax_example.permissions.core;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.disney4a.baymax.annotations.Tag_Activity;
import com.disney4a.baymax.core.app.activity.BaymaxActivity;
import com.disney4a.baymax_example.R;

/**
 * Created by Administrator on 2017/8/31 0031.
 */


@Tag_Activity(name = "home")
public class HomeActivity extends BaymaxActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions_core_home_layout);
    }
}
