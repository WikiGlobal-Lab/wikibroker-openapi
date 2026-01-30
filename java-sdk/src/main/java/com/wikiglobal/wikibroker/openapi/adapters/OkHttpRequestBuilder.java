package com.wikiglobal.wikibroker.openapi.adapters;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

import com.wikiglobal.wikibroker.openapi.common.types.LoadHeaders;
import com.wikiglobal.wikibroker.openapi.common.types.Sign;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class OkHttpRequestBuilder extends AbstractRequestBuilder<Request> {
    public OkHttpRequestBuilder(
        UUID apiKey,
        String apiSecret,
        LoadHeaders<Request> loadHeaders,
        Sign<Request> sign,
        Supplier<Instant> timestampGenerator,
        Supplier<UUID> idGenerator
    ) {
        super(apiKey, apiSecret, loadHeaders, sign, timestampGenerator, idGenerator);
    }

    @Override
    protected Request buildRequest() {
        return new Request.Builder().method(
                                        this.method,
                                        RequestBody.create(this.body.getBytes(StandardCharsets.UTF_8))
                                    )
                                    .headers(Headers.of(this.headers))
                                    .url(this.url)
                                    .build();
    }
}
