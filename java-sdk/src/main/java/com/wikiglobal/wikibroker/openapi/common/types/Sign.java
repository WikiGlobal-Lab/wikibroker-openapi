package com.wikiglobal.wikibroker.openapi.common.types;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@FunctionalInterface
public interface Sign<T> {
    void accept(
        RequestOperator<T> req,
        String key
    ) throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException;
}
