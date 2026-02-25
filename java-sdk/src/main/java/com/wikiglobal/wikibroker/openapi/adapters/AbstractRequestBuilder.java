package com.wikiglobal.wikibroker.openapi.adapters;

import com.wikiglobal.wikibroker.openapi.common.interfaces.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
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

@SuperBuilder
@Setter
@Accessors(chain = true, fluent = true)
public abstract class AbstractRequestBuilder<T> implements RequestOperator<T> {
    protected final Map<String, String> headers = new HashMap<>();
    @Getter protected String method;
    @Getter protected String url;
    @Getter protected String body;
    private final UUID apiKey;
    private final String apiSecret;
    private final LoadHeaders<T> loadHeaders;
    private final Sign<T> sign;
    private final Supplier<Instant> timestampGenerator;
    private final Supplier<UUID> idGenerator;

    @Override
    public final RequestBuilder<T> header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    @Override
    public final RequestBuilder<T> method(@NonNull String method) {
        this.method = method.toUpperCase();
        return this;
    }

    @Override
    public final <U> RequestBuilder<T> body(U body, @NonNull Function<U, String> serialize) {
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
    public final String header(String name) {
        return this.headers.get(name);
    }
}
