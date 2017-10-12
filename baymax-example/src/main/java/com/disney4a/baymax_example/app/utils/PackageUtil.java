package com.disney4a.baymax_example.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.disney4a.baymax.core.app.application.Baymax;

import java.io.File;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class PackageUtil {
    /**
     * @Description 系统源生安装应用
     * @param
     * @return
     */
    public static void installBySystem(Context context, String path) {

        if (context == null || path == null)
            return;

        Log.i("PackageUtil", "install apk file path:" + path);

        File f = new File(path);
        System.out.println("length=" + f.length());
        Uri mApkURI = Uri.fromFile(f);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Baymax.IntentConfig.NATIVE_FLAG, true);
        intent.setDataAndType(mApkURI,
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
