package com.wikiglobal.wikibroker.openapi.adapters;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.wikiglobal.wikibroker.openapi.common.types.LoadHeaders;
import com.wikiglobal.wikibroker.openapi.common.types.RequestOperator;
import com.wikiglobal.wikibroker.openapi.common.types.Sign;
import com.wikiglobal.wikibroker.openapi.common.types.Wrapper;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicHeader;

public class ApacheHttpRequestBuilder implements RequestOperator<ClassicHttpRequest>, Wrapper<ClassicRequestBuilder> {
    private final ClassicRequestBuilder raw;
    private final Map<String, String> headers;
    private String method;
    private String url;
    private String body;
    private final UUID apiKey;
    private final String apiSecret;
    private final LoadHeaders<ClassicHttpRequest> loadHeaders;
    private final Sign<ClassicHttpRequest> sign;
    private final Supplier<Instant> timestampGenerator;
    private final Supplier<UUID> idGenerator;

    public ApacheHttpRequestBuilder(
        ClassicRequestBuilder raw,
        UUID apiKey,
        String apiSecret,
        LoadHeaders<ClassicHttpRequest> loadHeaders,
        Sign<ClassicHttpRequest> sign,
        Supplier<Instant> timestampGenerator,
        Supplier<UUID> idGenerator
    ) {
        this.raw = raw;
        this.headers = new HashMap<>();
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.loadHeaders = loadHeaders;
        this.sign = sign;
        this.timestampGenerator = timestampGenerator;
        this.idGenerator = idGenerator;
    }

    @Override
    public ApacheHttpRequestBuilder setHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    @Override
    public ApacheHttpRequestBuilder setMethod(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    @Override
    public ApacheHttpRequestBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public ApacheHttpRequestBuilder setBody(String data) {
        this.body = data;
        return this;
    }

    @Override
    public ClassicHttpRequest build() throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        this.loadHeaders.accept(
            this,
            this.apiKey,
            this.timestampGenerator.get(),
            this.idGenerator.get()
        );
        this.sign.accept(this, this.apiSecret);
        final var headers = this.headers.entrySet()
                                        .stream()
                                        .map(entry -> new BasicHeader(
                                            entry.getKey(),
                                            entry.getValue()
                                        )).toArray(Header[]::new);
        return ClassicRequestBuilder.create(this.method)
                                    .setHeaders(headers)
                                    .setUri(this.url)
                                    .setEntity(this.body)
                                    .build();
    }

    @Override
    public ClassicRequestBuilder raw() {
        return this.raw;
    }

    @Override
    public String getHeader(String name) {
        return this.headers.get(name);
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getBody() {
        return this.body;
    }
}
