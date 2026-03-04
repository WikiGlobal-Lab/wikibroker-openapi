package com.wikiglobal.wikibroker.openapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.alibaba.fastjson2.JSON;
import com.wikiglobal.wikibroker.openapi.adapters.ApacheHttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.adapters.HttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.adapters.OkHttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.enums.CustomHeaders;
import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestBuilder;
import lombok.SneakyThrows;
import okhttp3.Request;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.wikiglobal.wikibroker.openapi.WikiBrokerOpenApi.RequestBuilderFactory;

public class OpenApiTest {
    private static final String baseURL = "https://api.example.com";
    private static final String path = "test?q1=c&q2=b&q1=a";

    @Contract(pure = true)
    private static @NonNull String url() {
        return String.format("%s/%s", OpenApiTest.baseURL, OpenApiTest.path);
    }

    private static final Map<String, String> body = Map.of("key", "value");
    private static final String method = "POST";
    private static final UUID apiKey = UUID.fromString("ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b");
    private static final String apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
    private static final UUID nonce = UUID.fromString("4428a206-1afd-4b15-a98d-43e91f49a08d");
    private static final Instant timestamp = Instant.ofEpochMilli(1798115622000L);
    private static final String expectedSignature = "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4";

    public OpenApiTest() {
    }

    @SneakyThrows
    private <T> T buildRequestWithJSON(@NonNull RequestBuilder<T> builder) {
        return builder.url(url()).method(method).body(body, JSON::toJSONString).build();
    }

    @SneakyThrows
    private <T> T buildRequestWithString(@NonNull RequestBuilder<T> builder) {
        return builder.url(url()).method(method).body(JSON.toJSONString(body)).build();
    }

    @Test
    @SneakyThrows
    void testNative() {
        final var builder = HttpRequestBuilder.builder()
                                              .apiKey(apiKey)
                                              .apiSecret(apiSecret)
                                              .loadHeaders(WikiBrokerOpenApi::addXHeaders)
                                              .sign(WikiBrokerOpenApi::sign)
                                              .timestampGenerator(() -> timestamp)
                                              .idGenerator(() -> nonce)
                                              .build();
        final var req = this.buildRequestWithJSON(builder);
        final var actualSignature = req.headers()
                                       .firstValue(CustomHeaders.Signature.value())
                                       .orElse("");
        assertEquals(expectedSignature, actualSignature);
    }

    @Test
    @SneakyThrows
    void testApache() {
        final var builder = ApacheHttpRequestBuilder.builder()
                                                    .apiKey(apiKey)
                                                    .apiSecret(apiSecret)
                                                    .loadHeaders(WikiBrokerOpenApi::addXHeaders)
                                                    .sign(WikiBrokerOpenApi::sign)
                                                    .timestampGenerator(() -> timestamp)
                                                    .idGenerator(() -> nonce)
                                                    .build();
        final var req = this.buildRequestWithJSON(builder);
        final var actualSignature = req.getHeader(CustomHeaders.Signature.value()).getValue();
        assertEquals(expectedSignature, actualSignature);
    }

    @Test
    @SneakyThrows
    void testOkHttp() {
        final var builder = OkHttpRequestBuilder.builder()
                                                .apiKey(apiKey)
                                                .apiSecret(apiSecret)
                                                .loadHeaders(WikiBrokerOpenApi::addXHeaders)
                                                .sign(WikiBrokerOpenApi::sign)
                                                .timestampGenerator(() -> timestamp)
                                                .idGenerator(() -> nonce)
                                                .build();
        final var req = this.buildRequestWithJSON(builder);
        final var actualSignature = req.header(CustomHeaders.Signature.value());
        assertEquals(expectedSignature, actualSignature);
    }

    @Test
    @SneakyThrows
    void testNativeFactory() {
        final var factory = new RequestBuilderFactory<HttpRequest>(
            apiKey.toString(),
            apiSecret,
            RequestBuilderFactory.BuiltInBuilder.Native.value()
        );
        final var builder = factory.create();
        final var req = this.buildRequestWithString(builder);
        final var actualSignature = req.headers()
                                       .firstValue(CustomHeaders.Signature.value())
                                       .orElse("");
        System.out.println(actualSignature);
    }

    @Test
    @SneakyThrows
    void testApacheFactory() {
        final var factory = new RequestBuilderFactory<ClassicHttpRequest>(
            apiKey.toString(),
            apiSecret,
            RequestBuilderFactory.BuiltInBuilder.Apache.value()
        );
        final var builder = factory.create();
        final var req = this.buildRequestWithString(builder);
        final var actualSignature = req.getHeader(CustomHeaders.Signature.value()).getValue();
        System.out.println(actualSignature);
    }

    @Test
    @SneakyThrows
    void testOkHttpFactory() {
        final var factory = new RequestBuilderFactory<Request>(
            apiKey.toString(),
            apiSecret,
            RequestBuilderFactory.BuiltInBuilder.OkHttp.value()
        );
        final var builder = factory.create();
        final var req = this.buildRequestWithString(builder);
        final var actualSignature = req.header(CustomHeaders.Signature.value());
        System.out.println(actualSignature);
    }
}
