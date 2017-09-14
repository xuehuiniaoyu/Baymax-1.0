package com.disney4a.baymax_example.app.activity.baymax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;

import com.disney4a.baymax.annotations.Tag_Activity;
import com.disney4a.baymax.core.app.activity.BaymaxCompatActivity;
import com.disney4a.baymax.core.app.application.Baymax;
import com.disney4a.baymax.utils.Reflect;
import com.disney4a.baymax.utils.ViewSelector;
import com.disney4a.baymax_example.R;
import com.disney4a.baymax_example.app.entity.UserInfo;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

@Tag_Activity(name = "testReflect")
public class TestReflect extends BaymaxCompatActivity {
    @ViewSelector.GetViewById(R.id.text1)
    private AppCompatTextView text1;

    @ViewSelector.GetViewById(R.id.text2)
    private AppCompatTextView text2;

    @ViewSelector.GetViewById(R.id.text3)
    private AppCompatTextView text3;

    @ViewSelector.GetViewById(R.id.text4)
    private AppCompatTextView text4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_reflect_layout);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("lilei");

        Reflect reflect = Reflect.newInstance();
        String userName = reflect.on(userInfo).get("userName");
        text1.setText("userName："+userName);

        reflect.clear().on(userInfo).method("setUserName", String.class).invoke("hanmeimei");
        text2.setText("userName："+userInfo.getUserName());

        Object activityThread = reflect.clear().on(Baymax.single().getSysHook()).get("activityThread");
        Object obj = reflect.clear().on(activityThread).get("mBoundApplication.info.mPackageName");
        text3.setText("obj="+obj);

        UserInfo userInfo1 = reflect.clear().on("com.disney4a.baymax_example.app.entity.UserInfo")
                .constructor(String.class)
                .newInstance("操作员");
        text4.setText(userInfo1.getUserName());
    }
}
