package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.common.enums.CustomHeaders;
import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestBuilder;
import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestOperator;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.UUID;

@UtilityClass
public class WikiBrokerOpenApi {
    public <T> void addXHeaders(
        @NonNull RequestBuilder<T> builder,
        @NonNull UUID apiKey,
        @NonNull Instant timestamp,
        @NonNull UUID nonce
    ) {
        builder.header(CustomHeaders.ApiKey.value(), apiKey.toString())
               .header(CustomHeaders.TimeStamp.value(), String.valueOf(timestamp.toEpochMilli()))
               .header(CustomHeaders.Nonce.value(), nonce.toString());
    }

    public <T> void sign(
        RequestOperator<T> req,
        String key
    ) throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        final var canonicalString = Core.generateCanonicalString(req);
        final var signature = Core.generateSignature(key, canonicalString);
        req.header(CustomHeaders.Signature.value(), signature);
    }
}
