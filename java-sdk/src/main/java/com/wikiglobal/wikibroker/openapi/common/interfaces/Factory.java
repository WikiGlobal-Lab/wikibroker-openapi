package com.wikiglobal.wikibroker.openapi.common.interfaces;

public interface Factory<T> {
    enum Type {
        Native(), OkHttp(), Apache();
    }

    T create();
}
