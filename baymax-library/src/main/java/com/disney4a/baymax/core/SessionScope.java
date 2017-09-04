package com.disney4a.baymax.core;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class SessionScope extends MapScope {

    private long defaultValidTime = 1000 * 60 * 30;

    private SessionScope() {}

    /**
     * 保存方法
     * @param key
     * @param value
     * @return
     */
    @Override
    public Object put(String key, Object value) {
        return super.put(key, value, defaultValidTime);
    }

    private static SessionScope instance;
    public static SessionScope single() {
        if(instance == null) {
            instance = new SessionScope();
        }
        return instance;
    }
}
