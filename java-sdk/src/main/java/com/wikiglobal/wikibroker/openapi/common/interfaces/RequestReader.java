package com.wikiglobal.wikibroker.openapi.common.interfaces;

public interface RequestReader {
    String header(String name);

    String method();

    String url();

    String body();
}
