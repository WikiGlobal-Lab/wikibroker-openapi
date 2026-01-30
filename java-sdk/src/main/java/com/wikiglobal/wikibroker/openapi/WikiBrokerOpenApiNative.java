package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.adapters.HttpRequestBuilder;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.UUID;

@SuppressWarnings("unused")
public class WikiBrokerOpenApiNative {
    private WikiBrokerOpenApiNative() {
    }

    public static @NonNull HttpRequestBuilder create(String apiKey, String apiSecret) {
        return new HttpRequestBuilder(
            UUID.fromString(apiKey),
            apiSecret,
            WikiBrokerOpenApi::addXHeaders,
            WikiBrokerOpenApi::sign,
            Instant::now,
            UUID::randomUUID
        );
    }
}
