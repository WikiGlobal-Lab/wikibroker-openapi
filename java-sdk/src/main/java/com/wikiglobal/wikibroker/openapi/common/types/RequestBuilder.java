package com.wikiglobal.wikibroker.openapi.common.types;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface RequestBuilder<T> {
    RequestBuilder<T> setHeader(String name, String value);

    RequestBuilder<T> setMethod(String method);

    RequestBuilder<T> setUrl(String url);

    RequestBuilder<T> setBody(String data);

    T build() throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException;
}
