package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.common.enums.CustomHeaders;
import com.wikiglobal.wikibroker.openapi.common.types.RequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.types.RequestOperator;
import org.jspecify.annotations.NonNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.UUID;

public class WikiBrokerOpenApi {
    private WikiBrokerOpenApi() {
    }

    public static <T> void addXHeaders(
        @NonNull RequestBuilder<T> builder,
        @NonNull UUID apiKey,
        @NonNull Instant timestamp,
        @NonNull UUID nonce
    ) {
        builder.setHeader(CustomHeaders.ApiKey.value(), apiKey.toString())
               .setHeader(CustomHeaders.TimeStamp.value(), String.valueOf(timestamp.toEpochMilli()))
               .setHeader(CustomHeaders.Nonce.value(), nonce.toString());
    }

    public static <T> void sign(
        RequestOperator<T> req,
        String key
    ) throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        final var canonicalString = Core.generateCanonicalString(req);
        final var signature = Core.generateSignature(key, canonicalString);
        req.setHeader(CustomHeaders.Signature.value(), signature);
    }
}
