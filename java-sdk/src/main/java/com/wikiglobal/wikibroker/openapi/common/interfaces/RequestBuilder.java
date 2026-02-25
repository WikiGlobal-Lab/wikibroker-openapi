package com.wikiglobal.wikibroker.openapi.common.interfaces;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

public interface RequestBuilder<T> {
    RequestBuilder<T> header(String name, String value);

    RequestBuilder<T> method(String method);

    RequestBuilder<T> url(String url);

    RequestBuilder<T> body(String body);

    <U> RequestBuilder<T> body(U body, Function<U, String> serialize);

    T build() throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException;
}
