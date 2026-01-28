package com.wikiglobal.wikibroker.openapi.common.enums;

public enum CustomHeaders {
    ApiKey("X-Api-Key"),
    TimeStamp("X-Timestamp"),
    Nonce("X-Nonce"),
    Signature("X-Signature");

    private final String value;

    CustomHeaders(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
