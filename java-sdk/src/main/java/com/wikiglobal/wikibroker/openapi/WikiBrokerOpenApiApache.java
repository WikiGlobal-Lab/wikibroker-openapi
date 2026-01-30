package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.adapters.ApacheHttpRequestBuilder;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.UUID;

@SuppressWarnings("unused")
public class WikiBrokerOpenApiApache {
    private WikiBrokerOpenApiApache() {
    }

    public static @NonNull ApacheHttpRequestBuilder create(String apiKey, String apiSecret) {
        return new ApacheHttpRequestBuilder(
            UUID.fromString(apiKey),
            apiSecret,
            WikiBrokerOpenApi::addXHeaders,
            WikiBrokerOpenApi::sign,
            Instant::now,
            UUID::randomUUID
        );
    }
}
