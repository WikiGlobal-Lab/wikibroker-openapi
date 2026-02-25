package com.wikiglobal.wikibroker.openapi.adapters;

import lombok.experimental.SuperBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicHeader;

@SuperBuilder
public final class ApacheHttpRequestBuilder extends AbstractRequestBuilder<ClassicHttpRequest> {
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
