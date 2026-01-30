package com.wikiglobal.wikibroker.openapi.adapters;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.wikiglobal.wikibroker.openapi.common.types.LoadHeaders;
import com.wikiglobal.wikibroker.openapi.common.types.Sign;

public final class HttpRequestBuilder extends AbstractRequestBuilder<HttpRequest> {
    public HttpRequestBuilder(
        UUID apiKey,
        String apiSecret,
        LoadHeaders<HttpRequest> loadHeaders,
        Sign<HttpRequest> sign,
        Supplier<Instant> timestampGenerator,
        Supplier<UUID> idGenerator
    ) {
        super(apiKey, apiSecret, loadHeaders, sign, timestampGenerator, idGenerator);
    }

    @Override
    protected HttpRequest buildRequest() {
        final var headers = this.headers.entrySet()
                                        .stream()
                                        .flatMap(entry -> Stream.of(
                                            entry.getKey(),
                                            entry.getValue()
                                        ))
                                        .toArray(String[]::new);
        return HttpRequest.newBuilder(URI.create(this.url))
                          .method(this.method, HttpRequest.BodyPublishers.ofString(this.body))
                          .headers(headers)
                          .build();
    }
}
