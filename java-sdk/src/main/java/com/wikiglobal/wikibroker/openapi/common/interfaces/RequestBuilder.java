package com.wikiglobal.wikibroker.openapi.common.interfaces;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

public interface RequestBuilder<T> {
    RequestBuilder<T> setHeader(String name, String value);

    RequestBuilder<T> setMethod(String method);

    RequestBuilder<T> setUrl(String url);

    RequestBuilder<T> setBody(String body);

    <U> RequestBuilder<T> setBody(U body, Function<U, String> serialize);

    T build() throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException;
}
