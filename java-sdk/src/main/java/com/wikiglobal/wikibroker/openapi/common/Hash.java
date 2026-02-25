package com.wikiglobal.wikibroker.openapi.common;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class Hash {
    public byte[] hmacSha256(
        @NonNull String key,
        @NonNull String message
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        final var restoreKey = new SecretKeySpec(
            key.getBytes(StandardCharsets.UTF_8),
            "HmacSHA256"
        );
        final var mac = Mac.getInstance(restoreKey.getAlgorithm());
        mac.init(restoreKey);
        return mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] sha256Hash(@NonNull String message) throws NoSuchAlgorithmException {
        final var md = MessageDigest.getInstance("SHA-256");
        md.update(message.getBytes(StandardCharsets.UTF_8));
        return md.digest();
    }
}
