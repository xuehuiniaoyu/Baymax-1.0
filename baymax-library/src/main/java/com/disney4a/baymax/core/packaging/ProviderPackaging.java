package com.disney4a.baymax.core.packaging;

import android.content.ContentProvider;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class ProviderPackaging {
    private String name;
    private boolean exported;
    private Class<? extends ContentProvider> provider;

    public ProviderPackaging(String name, Class<? extends ContentProvider> provider) {
        this.name = name;
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public Class<? extends ContentProvider> getProvider() {
        return provider;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }
}
