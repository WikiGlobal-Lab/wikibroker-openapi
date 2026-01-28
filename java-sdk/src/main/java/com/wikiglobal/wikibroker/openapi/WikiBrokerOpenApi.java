package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.common.enums.CustomHeaders;
import com.wikiglobal.wikibroker.openapi.common.types.HeadersLike;
import com.wikiglobal.wikibroker.openapi.common.types.RequestsInfo;
import org.jspecify.annotations.NonNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.UUID;

@SuppressWarnings("unused")
public class WikiBrokerOpenApi {
    private WikiBrokerOpenApi() {
    }

    public static void addXHeaders(
            @NonNull HeadersLike headers,
            @NonNull UUID apiKey,
            @NonNull Instant timestamp,
            @NonNull UUID nonce
    ) {
        headers.set(CustomHeaders.ApiKey.value(), apiKey.toString());
        headers.set(CustomHeaders.TimeStamp.value(), String.valueOf(timestamp.toEpochMilli()));
        headers.set(CustomHeaders.Nonce.value(), nonce.toString());
    }

    public static void sign(
            RequestsInfo req,
            String key
    ) throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        final var canonicalString = Core.generateCanonicalString(req);
        final var signature = Core.generateSignature(key, canonicalString);
        req.headers().set(CustomHeaders.Signature.value(), signature);
    }
}
