package com.disney4a.baymax_example.app.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class DbEntity extends DataSupport {
    private long id;
    public long getId() {
        if(id != 0)
            return id;
        return getBaseObjId();
    }

    public void setId(long id) {
        this.id = id;
    }
}
