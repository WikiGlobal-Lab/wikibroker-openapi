package com.wikiglobal.wikibroker.openapi.common.interfaces;

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
