package com.disney4a.baymax_example.app.activity.baymax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.disney4a.baymax.annotations.Tag_Activity;
import com.disney4a.baymax.core.app.activity.BaymaxActivity;
import com.disney4a.baymax.core.app.application.Baymax;
import com.disney4a.baymax.utils.ViewSelector;
import com.disney4a.baymax_example.R;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

@Tag_Activity(name = "permissionsManagement")
public class PermissionsManagementActivity extends BaymaxActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions_manage_layout);
    }

    @ViewSelector.OnClick(ids = {R.id.adminBtn, R.id.userBtn, R.id.coreBtn})
    public void onClick(View v) {
        if(v.getId() == R.id.adminBtn) {

            replace("com.disney4a.baymax_example.permissions.admin");
        }
        else if(v.getId() == R.id.userBtn)  {
            replace("com.disney4a.baymax_example.permissions.user");
        }
        else {
            replace("com.disney4a.baymax_example.permissions.core");
        }
        jump2Home();
    }

    void replace(String pkgName) {
        Baymax.single().setAnnotationsPackage(pkgName).play();
    }

    void jump2Home() {
        Baymax.single().activity("home").start();
    }
}
