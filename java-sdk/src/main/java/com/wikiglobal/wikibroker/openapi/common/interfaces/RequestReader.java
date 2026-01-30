package com.wikiglobal.wikibroker.openapi.common.interfaces;

public interface RequestReader {
    String getHeader(String name);

    String getMethod();

    String getUrl();

    String getBody();
}
