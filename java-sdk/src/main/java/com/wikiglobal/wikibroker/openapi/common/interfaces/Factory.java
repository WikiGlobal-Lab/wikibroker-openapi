package com.wikiglobal.wikibroker.openapi.common.interfaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;


public interface Factory<T> {
    @AllArgsConstructor
    enum BuiltInBuilder {
        Native("com.wikiglobal.wikibroker.openapi.adapters.HttpRequestBuilder"),
        OkHttp("com.wikiglobal.wikibroker.openapi.adapters.OkHttpRequestBuilder"),
        Apache("com.wikiglobal.wikibroker.openapi.adapters.ApacheHttpRequestBuilder");

        @Getter
        @Accessors(fluent = true)
        private final String value;
    }

    T create();
}
