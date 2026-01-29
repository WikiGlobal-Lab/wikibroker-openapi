package com.wikiglobal.wikibroker.openapi.common.types;

public interface RequestReader {
    String getHeader(String name);

    String getMethod();

    String getUrl();

    String getBody();
}
