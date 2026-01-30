package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.common.Hash;
import com.wikiglobal.wikibroker.openapi.common.Utils;
import com.wikiglobal.wikibroker.openapi.common.enums.CustomHeaders;

import com.wikiglobal.wikibroker.openapi.common.types.RequestReader;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public final class Core {
    private Core() {
    }

    @Contract("_, _ -> new")
    public static @NonNull String generateSignature(
        @NonNull String key,
        @NonNull String message
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        return Hex.encodeHexString(Hash.hmacSha256(key, message));
    }

    public static @NonNull String generateCanonicalString(
        @NonNull RequestReader req
    ) throws MalformedURLException, URISyntaxException, NoSuchAlgorithmException {
        final var method = req.getMethod().toUpperCase();
        final var path = new URI(req.getUrl()).toURL().getPath();
        final var canonicalQuery = Core.buildCanonicalQuery(req);
        final var apiKey = req.getHeader(CustomHeaders.ApiKey.value());
        final var timestamp = req.getHeader(CustomHeaders.TimeStamp.value());
        final var nonce = req.getHeader(CustomHeaders.Nonce.value());
        final var bodyHash = Core.calculateBodyHash(req);
        return String.join("\n", method, path, canonicalQuery, apiKey, timestamp, nonce, bodyHash);
    }

    private static @NonNull String calculateBodyHash(@NonNull RequestReader req) throws NoSuchAlgorithmException {
        final var body = Utils.isRequestUsePostMethod(req) ? req.getBody() : "";
        final var hash = Hash.sha256Hash(body);
        return Hex.encodeHexString(hash);
    }

    private static @NonNull String buildCanonicalQuery(
        @NonNull RequestReader req
    ) throws URISyntaxException, MalformedURLException {
        final var queryString = new URI(req.getUrl()).toURL().getQuery();
        final var query = Arrays.stream(queryString.split("&"))
                                .map(pair -> pair.split("="))
                                .collect(Collectors.groupingBy(
                                    x -> x[0],
                                    Collectors.mapping(
                                        x -> x[1],
                                        Collectors.toList()
                                    )
                                ));
        for (var pair : query.entrySet()) {
            pair.getValue().sort(Comparator.naturalOrder());
        }
        final var parts = new ArrayList<>(query.entrySet());
        parts.sort(Map.Entry.comparingByKey());
        return parts.stream().flatMap(item -> {
            final var values = item.getValue();
            return values.stream().map(value -> String.format("%s=%s", item.getKey(), value));
        }).collect(Collectors.joining("&"));
    }
}
