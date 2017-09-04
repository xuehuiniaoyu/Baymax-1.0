package com.disney4a.baymax.core;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class RequestScope extends MapScope {
    private RequestScope() {}

    @Override
    public <T> T get(String key) {
        return super.remove(key);
    }

    private static RequestScope instance;
    public static RequestScope single() {
        if(instance == null) {
            instance = new RequestScope();
        }
        return instance;
    }
}
