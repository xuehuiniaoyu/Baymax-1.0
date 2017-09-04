package com.disney4a.baymax.core.attr;

import com.disney4a.baymax.core.packaging.ControllerPackaging;

/**
 * Created by Administrator on 2017/8/20 0020.
 * 通信员，作用：可以做一些事情。
 */

public class Correspondents {
    private String uri;
    private ControllerPackaging packaging;
    private Expectation expectation;
    private Object body;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPackaging(ControllerPackaging packaging) {
        this.packaging = packaging;
    }

    public ControllerPackaging getPackaging() {
        return packaging;
    }

    public Expectation getExpectation() {
        return expectation;
    }

    public void setExpectation(Expectation expectation) {
        this.expectation = expectation;
    }

    public <T> T getBody() {
        return (T) body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    /**
     * 出发，执行多态方法。
     */
    public void go() {
        if(this.expectation == null) {
            throw new UnsupportedOperationException("Please call 'setExpectation' first!");
        }
        this.expectation.doSomething(this);
    }
}
