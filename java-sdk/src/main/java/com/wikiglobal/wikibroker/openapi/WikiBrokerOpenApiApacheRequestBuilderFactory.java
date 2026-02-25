package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.adapters.ApacheHttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.jspecify.annotations.NonNull;

public final class WikiBrokerOpenApiApacheRequestBuilderFactory extends AbstractBuilderFactory<ClassicHttpRequest> {
    public WikiBrokerOpenApiApacheRequestBuilderFactory(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    @Override
    public @NonNull RequestBuilder<ClassicHttpRequest> create() {
        return ApacheHttpRequestBuilder.builder()
                                       .apiKey(this.apiKey)
                                       .apiSecret(this.apiSecret)
                                       .loadHeaders(this.loadHeaders)
                                       .sign(this.sign)
                                       .timestampGenerator(this.timestampGenerator)
                                       .idGenerator(this.idGenerator)
                                       .build();
    }
}
