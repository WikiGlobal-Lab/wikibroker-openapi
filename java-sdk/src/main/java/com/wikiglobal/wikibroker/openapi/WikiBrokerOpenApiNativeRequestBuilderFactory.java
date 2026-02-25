package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.adapters.HttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestBuilder;
import org.jspecify.annotations.NonNull;

import java.net.http.HttpRequest;

public final class WikiBrokerOpenApiNativeRequestBuilderFactory extends AbstractBuilderFactory<HttpRequest> {
    public WikiBrokerOpenApiNativeRequestBuilderFactory(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    @Override
    public @NonNull RequestBuilder<HttpRequest> create() {
        return HttpRequestBuilder.builder()
                                 .apiKey(this.apiKey)
                                 .apiSecret(this.apiSecret)
                                 .loadHeaders(this.loadHeaders)
                                 .sign(this.sign)
                                 .timestampGenerator(this.timestampGenerator)
                                 .idGenerator(this.idGenerator)
                                 .build();
    }
}
