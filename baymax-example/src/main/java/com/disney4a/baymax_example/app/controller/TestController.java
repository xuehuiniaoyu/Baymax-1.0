package com.disney4a.baymax_example.app.controller;

import android.util.Log;
import android.widget.TextView;

import com.disney4a.baymax.annotations.Tag_Controller;
import com.disney4a.baymax.annotations.Tag_Http;
import com.disney4a.baymax.annotations.Tag_Return;
import com.disney4a.baymax.core.RequestScope;
import com.disney4a.baymax.core.attr.Correspondents;
import com.disney4a.baymax.core.attr.impl.HttpExpectation;
import com.disney4a.baymax.core.attr.impl.HttpRepay;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

@Tag_Controller(uri = "http://www.baidu.com")
public class TestController {

    public static final String TAG = TestController.class.getSimpleName();

    /**
     * 访问百度
     * @param correspondents
     */
    @Tag_Controller.Path(name = "getBaidyContent")
    @Tag_Http
    public void baidu(Correspondents correspondents, final TextView textView) {
        correspondents.setExpectation(new HttpExpectation() {
            @Override
            public void onRepay(HttpRepay repay) {
                try {
                    final String result = System.currentTimeMillis()+" - "+repay.getResponse().body().string();
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(result);
                        }
                    });
                } catch (Exception e) {

                }
            }
        });
        correspondents.go();
    }

    @Tag_Controller.Path(name = "list", value = "/test.xml")
    @Tag_Return(type = Tag_Return.REDIRECT)
    public String jump(Correspondents correspondents, String name, int age, Object obj) {
        Log.i(TAG, "name="+name + " age="+age + " obj="+obj);
        RequestScope.single().put("name", name);
        RequestScope.single().put("age", age);
        RequestScope.single().put("obj", obj);
        return "test-activity-result";
    }
}
