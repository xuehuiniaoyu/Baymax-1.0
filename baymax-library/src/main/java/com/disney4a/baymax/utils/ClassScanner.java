package com.disney4a.baymax.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public class ClassScanner {

    public static List<Class<?>> getAllClasses(String packageName) {
        return getAllClasses(packageName, Thread.currentThread().getContextClassLoader());
    }

    public static List<Class<?>> getAllClasses(String packageName, ClassLoader classLoader) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            Reflect reflect = Reflect.newInstance();
            Object pathList = reflect.on(classLoader).get("pathList");
            Object[] dexElements = reflect.clear().on(pathList).get("dexElements");
            for(Object dexElement : dexElements) {
                DexFile df = reflect.clear().on(dexElement).get("dexFile");
                Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
                while (enumeration.hasMoreElements()) {//遍历
                    String className = enumeration.nextElement();
                    if (className.contains(packageName)) {//在当前所有可执行的类里面查找包含有该包名的所有类
                        classes.add(classLoader.loadClass(className));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}
