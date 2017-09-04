package com.disney4a.baymax_example.app.activity.protozoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.disney4a.baymax.core.app.activity.BaymaxActivity;
import com.disney4a.baymax.utils.ViewSelector;
import com.disney4a.baymax_example.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

public class WelcomeActivity extends BaymaxActivity {
    @ViewSelector.GetViewById(R.id.timerText)
    private TextView timerText;

    final int MAX_NUM = 5;
    private int num;

    private Timer mTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        mTimer = new Timer("loading...");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(num == MAX_NUM) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jump2Main();
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerText.setText("还有 "+(MAX_NUM-(num++))+" 秒进入");
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    void jump2Main() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("hello", "我是从WelcomeActivity传递过来的参数！");
        startActivity(intent);
        mTimer.cancel();
        mTimer = null;
        this.finish();
    }
}
