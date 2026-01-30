package com.wikiglobal.wikibroker.openapi.adapters;

import com.wikiglobal.wikibroker.openapi.common.interfaces.*;
import org.jspecify.annotations.NonNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractRequestBuilder<T> implements RequestOperator<T> {
    protected final Map<String, String> headers;
    protected String method;
    protected String url;
    protected String body;
    private final UUID apiKey;
    private final String apiSecret;
    private final LoadHeaders<T> loadHeaders;
    private final Sign<T> sign;
    private final Supplier<Instant> timestampGenerator;
    private final Supplier<UUID> idGenerator;

    public AbstractRequestBuilder(
        UUID apiKey,
        String apiSecret,
        LoadHeaders<T> loadHeaders,
        Sign<T> sign,
        Supplier<Instant> timestampGenerator,
        Supplier<UUID> idGenerator
    ) {
        this.headers = new HashMap<>();
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.loadHeaders = loadHeaders;
        this.sign = sign;
        this.timestampGenerator = timestampGenerator;
        this.idGenerator = idGenerator;
    }

    @Override
    public final RequestBuilder<T> setHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    @Override
    public final RequestBuilder<T> setMethod(@NonNull String method) {
        this.method = method.toUpperCase();
        return this;
    }

    @Override
    public final RequestBuilder<T> setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public final RequestBuilder<T> setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public final <U> RequestBuilder<T> setBody(U body, @NonNull Function<U, String> serialize) {
        this.body = serialize.apply(body);
        return this;
    }

    @Override
    public final T build() throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        this.loadHeaders.accept(
            this,
            this.apiKey,
            this.timestampGenerator.get(),
            this.idGenerator.get()
        );
        this.sign.accept(this, this.apiSecret);
        return buildRequest();
    }

    protected abstract T buildRequest();

    @Override
    public final String getHeader(String name) {
        return this.headers.get(name);
    }

    @Override
    public final String getMethod() {
        return this.method;
    }

    @Override
    public final String getUrl() {
        return this.url;
    }

    @Override
    public final String getBody() {
        return this.body;
    }
}
