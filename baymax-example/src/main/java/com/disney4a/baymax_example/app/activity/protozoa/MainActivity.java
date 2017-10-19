package com.disney4a.baymax_example.app.activity.protozoa;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.disney4a.baymax.annotations.Tag_Activity;
import com.disney4a.baymax.core.app.activity.BaymaxCompatActivity;
import com.disney4a.baymax.core.app.application.Baymax;
import com.disney4a.baymax.utils.ViewSelector;
import com.disney4a.baymax_example.R;
import com.disney4a.baymax_example.app.utils.PackageUtil;

import java.io.File;
import java.util.Timer;

@Tag_Activity(name = "main")
public class MainActivity extends BaymaxCompatActivity {
    @ViewSelector.GetViewById(R.id.textView)
    private AppCompatTextView textView;

    @ViewSelector.GetViewById(R.id.baiduResult)
    private AppCompatTextView baiduResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView.setText("value="+getIntent().getStringExtra("hello"));
    }

    @ViewSelector.OnClick(ids = {
            R.id.testStartActivity,
            R.id.testStartActivityResult,
            R.id.testStartService,
            R.id.testStartProvider,
            R.id.testStartController,
            R.id.testDB,
            R.id.testReflect,
            R.id.permissionsManagement,
            R.id.systemInstaller
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testStartActivity: {
                Baymax.single().activity("test-activity").start();
                break;
            }
            case R.id.testStartActivityResult: {
                Baymax.single().controller("list").execute("张三", 38, new Timer());
                break;
            }
            case R.id.testStartService: {
                Bundle bundle = new Bundle();
                bundle.putString("hello", "Hello Baymax");
                Baymax.single().service("test-service").start(bundle);
                break;
            }
            case R.id.testStartProvider: {
                ContentResolver contentResolver = getContentResolver();
                contentResolver.query(Uri.parse("content://testProvider?name=张三&性别=男&年龄=38"), null, null, null, null);
                break;
            }
            case R.id.testStartController: {
                Baymax.single().execute("getBaidyContent", baiduResult);
                break;
            }
            case R.id.testDB: {
                Baymax.single().activity("dbTest").start();
                break;
            }
            case R.id.testReflect: {
                Baymax.single().activity("testReflect").start();
                break;
            }
            case R.id.permissionsManagement: {
                Baymax.single().activity("permissionsManagement").start();
                break;
            }
            case R.id.systemInstaller: {
                PackageUtil.installBySystem(this, new File(getCacheDir(), "a.apk").getAbsolutePath());
                break;
            }
        }
    }
}
