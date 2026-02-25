package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.common.interfaces.*;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class AbstractBuilderFactory<T> implements Factory<RequestBuilder<T>> {
    protected final UUID apiKey;
    protected final String apiSecret;
    protected final LoadHeaders<T> loadHeaders = WikiBrokerOpenApi::addXHeaders;
    protected final Sign<T> sign = WikiBrokerOpenApi::sign;
    protected final Supplier<Instant> timestampGenerator = Instant::now;
    protected final Supplier<UUID> idGenerator = UUID::randomUUID;

    public AbstractBuilderFactory(String apiKey, String apiSecret) {
        this.apiKey = UUID.fromString(apiKey);
        this.apiSecret = apiSecret;
    }

    @Override
    public abstract RequestBuilder<T> create();
}
