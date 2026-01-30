package com.wikiglobal.wikibroker.openapi.common.types;

import java.time.Instant;
import java.util.UUID;

@FunctionalInterface
public interface LoadHeaders<T> {
    void accept(
        RequestBuilder<T> builder,
        UUID apiKey,
        Instant timestamp,
        UUID nonce
    );
}
