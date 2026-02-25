package com.wikiglobal.wikibroker.openapi.adapters;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.stream.Stream;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public final class HttpRequestBuilder extends AbstractRequestBuilder<HttpRequest> {
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
