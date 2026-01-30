package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.adapters.OkHttpRequestBuilder;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.UUID;

@SuppressWarnings("unused")
public class WikiBrokerOpenApiOkhttp {
    private WikiBrokerOpenApiOkhttp() {
    }

    public static @NonNull OkHttpRequestBuilder create(String apiKey, String apiSecret) {
        return new OkHttpRequestBuilder(
            UUID.fromString(apiKey),
            apiSecret,
            WikiBrokerOpenApi::addXHeaders,
            WikiBrokerOpenApi::sign,
            Instant::now,
            UUID::randomUUID
        );
    }
}
