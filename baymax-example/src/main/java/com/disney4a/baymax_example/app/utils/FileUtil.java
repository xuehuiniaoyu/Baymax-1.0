package com.disney4a.baymax_example.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class FileUtil {
    /**
     * @param src
     * @param dist
     */
    public static void copy(String src, String dist) throws FileNotFoundException {
        copy(new File(src), new File(dist));
    }


    /**
     * 拷贝文件
     *
     * @param src
     * @param dist
     */
    public static void copy(File src, File dist) throws FileNotFoundException {
        copy(new FileInputStream(src), new FileOutputStream(dist));
    }

    public static void copy(InputStream ins, OutputStream outs) {
        int len = -1;
        byte[] b = new byte[1024];
        try {
            while ((len = ins.read(b)) != -1) {
                outs.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
