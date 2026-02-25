package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.adapters.OkHttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestBuilder;
import okhttp3.Request;

public class WikiBrokerOpenApiOkhttpRequestBuilderFactory extends AbstractBuilderFactory<Request> {
    public WikiBrokerOpenApiOkhttpRequestBuilderFactory(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    @Override
    public RequestBuilder<Request> create() {
        return OkHttpRequestBuilder.builder()
                                   .apiKey(this.apiKey)
                                   .apiSecret(this.apiSecret)
                                   .loadHeaders(this.loadHeaders)
                                   .sign(this.sign)
                                   .timestampGenerator(this.timestampGenerator)
                                   .idGenerator(this.idGenerator)
                                   .build();
    }
}
