package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.adapters.HttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestBuilder;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.net.http.HttpRequest;

@SuppressWarnings("unused")
public final class WikiBrokerOpenApiNativeRequestBuilderFactory extends AbstractBuilderFactory<HttpRequest> {
    public WikiBrokerOpenApiNativeRequestBuilderFactory(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    @Contract(" -> new")
    @Override
    public @NonNull RequestBuilder<HttpRequest> create() {
        return new HttpRequestBuilder(
            this.apiKey,
            this.apiSecret,
            this.loadHeaders,
            this.sign,
            this.timestampGenerator,
            this.idGenerator
        );
    }
}
