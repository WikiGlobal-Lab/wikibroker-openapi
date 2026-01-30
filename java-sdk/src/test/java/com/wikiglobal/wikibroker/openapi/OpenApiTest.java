package com.wikiglobal.wikibroker.openapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.alibaba.fastjson2.JSON;
import com.wikiglobal.wikibroker.openapi.adapters.HttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.adapters.OkHttpRequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.enums.CustomHeaders;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class OpenApiTest {
    private static final String baseURL = "https://api.example.com";
    private static final String path = "test?q1=c&q2=b&q1=a";

    private static String url() {
        return String.format("%s/%s", OpenApiTest.baseURL, OpenApiTest.path);
    }

    private static final String body = JSON.toJSONString(Map.of("key", "value"));
    private static final String method = "POST";

    private static final UUID apiKey = UUID.fromString("ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b");
    private static final String apiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
    private static final UUID nonce = UUID.fromString("4428a206-1afd-4b15-a98d-43e91f49a08d");
    private static final Instant timestamp = Instant.ofEpochMilli(1798115622000L);
    private static final String expectedSignature = "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4";

    public OpenApiTest() {
    }

    @Test
    void testNative() {
        final var raw = HttpRequest.newBuilder();
        final var builder = new HttpRequestBuilder(
            raw,
            apiKey,
            apiSecret,
            WikiBrokerOpenApi::addXHeaders,
            WikiBrokerOpenApi::sign,
            () -> timestamp,
            () -> nonce
        );
        try {
            final var req = builder.setUrl(url()).setMethod(method).setBody(body).build();
            final var actualSignature = req.headers()
                                           .firstValue(CustomHeaders.Signature.value())
                                           .orElse("");
            assertEquals(expectedSignature, actualSignature);
        } catch (MalformedURLException |
                 URISyntaxException |
                 NoSuchAlgorithmException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testApache() {
        // TODO
    }

    @Test
    void testOkHttp() {
        final var raw = new Request.Builder();
        final var builder = new OkHttpRequestBuilder(
            raw,
            apiKey,
            apiSecret,
            WikiBrokerOpenApi::addXHeaders,
            WikiBrokerOpenApi::sign,
            () -> timestamp,
            () -> nonce
        );
        try {
            final var req = builder.setUrl(url()).setMethod(method).setBody(body).build();
            final var actualSignature = req.header(CustomHeaders.Signature.value());
            assertEquals(expectedSignature, actualSignature);
        } catch (MalformedURLException |
                 URISyntaxException |
                 NoSuchAlgorithmException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
