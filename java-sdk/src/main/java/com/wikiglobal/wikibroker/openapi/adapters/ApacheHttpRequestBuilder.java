package com.wikiglobal.wikibroker.openapi.adapters;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

import com.wikiglobal.wikibroker.openapi.common.interfaces.LoadHeaders;
import com.wikiglobal.wikibroker.openapi.common.interfaces.Sign;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicHeader;

public final class ApacheHttpRequestBuilder extends AbstractRequestBuilder<ClassicHttpRequest> {
    public ApacheHttpRequestBuilder(
        UUID apiKey,
        String apiSecret,
        LoadHeaders<ClassicHttpRequest> loadHeaders,
        Sign<ClassicHttpRequest> sign,
        Supplier<Instant> timestampGenerator,
        Supplier<UUID> idGenerator
    ) {
        super(apiKey, apiSecret, loadHeaders, sign, timestampGenerator, idGenerator);
    }

    @Override
    protected ClassicHttpRequest buildRequest() {
        final var headers = this.headers.entrySet()
                                        .stream()
                                        .map(entry -> new BasicHeader(
                                            entry.getKey(),
                                            entry.getValue()
                                        ))
                                        .toArray(Header[]::new);
        return ClassicRequestBuilder.create(this.method)
                                    .setHeaders(headers)
                                    .setUri(this.url)
                                    .setEntity(this.body)
                                    .build();
    }
}
