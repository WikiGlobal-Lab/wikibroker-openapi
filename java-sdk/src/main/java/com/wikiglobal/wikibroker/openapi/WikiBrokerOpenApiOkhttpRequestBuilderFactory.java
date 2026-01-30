package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.adapters.OkHttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestBuilder;
import okhttp3.Request;

@SuppressWarnings("unused")
public class WikiBrokerOpenApiOkhttpRequestBuilderFactory extends AbstractBuilderFactory<Request> {
    public WikiBrokerOpenApiOkhttpRequestBuilderFactory(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    @Override
    public RequestBuilder<Request> create() {
        return new OkHttpRequestBuilder(
            this.apiKey,
            this.apiSecret,
            this.loadHeaders,
            this.sign,
            this.timestampGenerator,
            this.idGenerator
        );
    }
}
