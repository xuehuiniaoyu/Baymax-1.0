package com.disney4a.baymax.core.packaging;

import com.disney4a.baymax.annotations.Tag_Http;
import com.disney4a.baymax.annotations.Tag_Return;
import com.disney4a.baymax.core.app.application.Baymax;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class ControllerPackaging {
    // 唯一名称
    private String name;
    // 服务器地址
    private String rootUri;
    // 接口地址
    private String path;
    private Tag_Http tag_http;
    private Tag_Return tag_return;

    private Class<?> fromClass;
    private Method method;

    private Object obj;

    public ControllerPackaging(String rootUri, String path) {
        this.rootUri = rootUri;
        this.path = path;
    }

    public Class<?> getFromClass() {
        return fromClass;
    }

    public void setFromClass(Class<?> fromClass) {
        this.fromClass = fromClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Tag_Http getTag_http() {
        return tag_http;
    }

    public void setTag_http(Tag_Http tag_http) {
        this.tag_http = tag_http;
    }

    public Tag_Return getTag_return() {
        return tag_return;
    }

    public void setTag_return(Tag_Return tag_return) {
        this.tag_return = tag_return;
    }

    /**
     * 得到全路径
     * @return
     */
    public String getFullPath() {
        if(path.contains("http://") || path.contains("https://")) {
            return path;
        }
        String webSite001 = rootUri;
        try {
            if ("/".equals(rootUri.substring(rootUri.length() - 1))) {
                webSite001 = webSite001.substring(0, webSite001.length() - 1);
            }
        } catch (Exception e) {

        }
        return webSite001 + path;
    }

    public String execute(Object ... args) {
        return Baymax.single().execute(name, args);
    }
}
