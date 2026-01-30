package com.wikiglobal.wikibroker.openapi.adapters;


import com.wikiglobal.wikibroker.openapi.common.types.LoadHeaders;
import com.wikiglobal.wikibroker.openapi.common.types.RequestOperator;
import com.wikiglobal.wikibroker.openapi.common.types.Sign;
import com.wikiglobal.wikibroker.openapi.common.types.Wrapper;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class HttpRequestBuilder implements RequestOperator<HttpRequest>, Wrapper<HttpRequest.Builder> {
    private final HttpRequest.Builder raw;
    private final Map<String, String> headers;
    private String method;
    private String url;
    private String body;
    private final UUID apiKey;
    private final String apiSecret;
    private final LoadHeaders<HttpRequest> loadHeaders;
    private final Sign<HttpRequest> sign;
    private final Supplier<Instant> timestampGenerator;
    private final Supplier<UUID> idGenerator;

    public HttpRequestBuilder(
        HttpRequest.Builder raw,
        UUID apiKey,
        String apiSecret,
        LoadHeaders<HttpRequest> loadHeaders,
        Sign<HttpRequest> sign,
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

    public HttpRequestBuilder setHeader(String name, String value) {
        this.headers.put(name, value);
        this.raw.setHeader(name, this.headers.get(name));
        return this;
    }

    public HttpRequestBuilder setMethod(String method) {
        this.method = method.toUpperCase();
        this.raw.method(this.method, HttpRequest.BodyPublishers.noBody());
        return this;
    }

    public HttpRequestBuilder setUrl(String url) {
        this.url = url;
        this.raw.uri(URI.create(this.url));
        return this;
    }

    public HttpRequestBuilder setBody(String data) {
        this.body = data;
        this.raw.method(this.method, HttpRequest.BodyPublishers.ofString(data));
        return this;
    }

    public HttpRequest build() throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        this.loadHeaders.accept(
            this,
            this.apiKey,
            this.timestampGenerator.get(),
            this.idGenerator.get()
        );
        this.sign.accept(this, this.apiSecret);
        return this.raw.build();
    }

    public HttpRequest.Builder raw() {
        return this.raw;
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public String getBody() {
        return this.body;
    }
}
