package com.wikiglobal.wikibroker.openapi.adapters;

import java.nio.charset.StandardCharsets;

import lombok.experimental.SuperBuilder;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jspecify.annotations.NonNull;

@SuperBuilder
public final class OkHttpRequestBuilder extends AbstractRequestBuilder<Request> {
    @Override
    protected @NonNull Request buildRequest() {
        return new Request.Builder().method(
                                        this.method,
                                        RequestBody.create(this.body.getBytes(StandardCharsets.UTF_8))
                                    )
                                    .headers(Headers.of(this.headers))
                                    .url(this.url)
                                    .build();
    }
}
