package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.common.interfaces.*;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class AbstractBuilderFactory<T> implements Factory<RequestBuilder<T>> {
    protected final UUID apiKey;
    protected final String apiSecret;
    protected final LoadHeaders<T> loadHeaders;
    protected final Sign<T> sign;
    protected final Supplier<Instant> timestampGenerator;
    protected final Supplier<UUID> idGenerator;

    public AbstractBuilderFactory(String apiKey, String apiSecret) {
        this.apiKey = UUID.fromString(apiKey);
        this.apiSecret = apiSecret;
        this.loadHeaders = WikiBrokerOpenApi::addXHeaders;
        this.sign = WikiBrokerOpenApi::sign;
        this.timestampGenerator = Instant::now;
        this.idGenerator = UUID::randomUUID;
    }

    @Override
    public abstract RequestBuilder<T> create();
}
