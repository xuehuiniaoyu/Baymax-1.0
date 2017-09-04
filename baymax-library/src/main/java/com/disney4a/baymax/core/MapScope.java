package com.disney4a.baymax.core;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class MapScope {

    protected class Pkg {
        long createTime;
        long validTime;
        Object obj;
    }

    MapScope() {}

    private final HashMap<String, Pkg> hashMap = new HashMap<>();

    public Object put(String key, Object value, long validTime) {
        Pkg pkg = new Pkg();
        pkg.createTime = System.currentTimeMillis();
        pkg.validTime = validTime;
        pkg.obj = value;
        return hashMap.put(key, pkg);
    }

    public Object put(String key, Object value) {
        return this.put(key, value, 0);
    }

    public <T> T get(String key) {
        Pkg pkg = hashMap.get(key);
        if(checkWhetherTheFailure(pkg)) {
            return (T) pkg.obj;
        }
        else {
            hashMap.remove(key);
            return null;
        }
    }

    public <T> T remove(String key) {
        Pkg pkg = hashMap.get(key);
        if(containsKey(key)) {
            return (T) pkg.obj;
        }
        return null;
    }

    private boolean checkWhetherTheFailure(Pkg pkg) {
        if(pkg != null) {
            if(pkg.validTime <= 0) {
                return true;
            }
            if(System.currentTimeMillis() - pkg.createTime <= pkg.validTime) {
                return true;
            }
        }
        return false;
    }

    public boolean containsKey(String key) {
        Pkg pkg = hashMap.get(key);
        if(checkWhetherTheFailure(pkg)) {
            return true;
        }
        else {
            hashMap.remove(key);
            return false;
        }
    }
}
