package com.disney4a.baymax_example.app.activity.baymax;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.disney4a.baymax.annotations.Tag_Activity;
import com.disney4a.baymax.core.RequestScope;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

@Tag_Activity(name = "test-activity-result")
public class TestActivityResult extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("TestActivityResult name="+ RequestScope.single().get("name")+", age="+RequestScope.single().get("age")+", obj="+RequestScope.single().get("obj"));
        setContentView(textView);
    }
}
