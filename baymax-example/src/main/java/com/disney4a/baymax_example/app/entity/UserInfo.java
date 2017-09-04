package com.disney4a.baymax_example.app.entity;

import org.litepal.annotation.Column;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class UserInfo extends DbEntity {
    @Column(unique = true)
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
