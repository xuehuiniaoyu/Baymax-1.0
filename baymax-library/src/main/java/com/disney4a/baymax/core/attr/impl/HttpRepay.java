package com.disney4a.baymax.core.attr.impl;


import com.disney4a.baymax.core.attr.Repay;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/21 0021.
 */

public class HttpRepay implements Repay {
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
