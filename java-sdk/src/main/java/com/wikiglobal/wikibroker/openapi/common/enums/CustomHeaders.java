package com.wikiglobal.wikibroker.openapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
public enum CustomHeaders {
    ApiKey("X-Api-Key"),
    TimeStamp("X-Timestamp"),
    Nonce("X-Nonce"),
    Signature("X-Signature");

    @Getter
    @Accessors(fluent = true)
    private final String value;
}
