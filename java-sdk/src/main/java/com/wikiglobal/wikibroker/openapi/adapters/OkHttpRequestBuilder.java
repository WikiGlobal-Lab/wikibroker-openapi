package com.wikiglobal.wikibroker.openapi.adapters;

import com.wikiglobal.wikibroker.openapi.common.types.LoadHeaders;
import com.wikiglobal.wikibroker.openapi.common.types.Sign;
import com.wikiglobal.wikibroker.openapi.common.types.RequestOperator;
import com.wikiglobal.wikibroker.openapi.common.types.Wrapper;
import okhttp3.Request;
import okhttp3.RequestBody;

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

public class OkHttpRequestBuilder implements RequestOperator<Request>, Wrapper<Request.Builder> {
    private final Request.Builder raw;
    private final Map<String, String> headers;
    private String method;
    private String url;
    private String body;
    private final UUID apiKey;
    private final String apiSecret;
    private final LoadHeaders<Request> loadHeaders;
    private final Sign<Request> sign;
    private final Supplier<Instant> timestampGenerator;
    private final Supplier<UUID> idGenerator;

    public OkHttpRequestBuilder(
        Request.Builder raw,
        UUID apiKey,
        String apiSecret,
        LoadHeaders<Request> loadHeaders,
        Sign<Request> sign,
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

    public OkHttpRequestBuilder setHeader(String name, String value) {
        this.headers.put(name, value);
        this.raw.header(name, this.headers.get(name));
        return this;
    }

    public OkHttpRequestBuilder setMethod(String method) {
        this.method = method.toUpperCase();
        this.raw.setMethod$okhttp(this.method);
        return this;
    }

    public OkHttpRequestBuilder setUrl(String url) {
        this.url = url;
        this.raw.url(this.url);
        return this;
    }

    public OkHttpRequestBuilder setBody(String data) {
        this.body = data;
        this.raw.setBody$okhttp(RequestBody.create(this.body.getBytes(StandardCharsets.UTF_8)));
        return this;
    }

    public Request build() throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        this.loadHeaders.accept(
            this,
            this.apiKey,
            this.timestampGenerator.get(),
            this.idGenerator.get()
        );
        this.sign.accept(this, this.apiSecret);
        return this.raw.build();
    }

    public Request.Builder raw() {
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
