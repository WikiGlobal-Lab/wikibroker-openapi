package com.wikiglobal.wikibroker.openapi.common;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class Hash {
    public byte[] hmacSha256(
        byte[] key,
        byte[] message
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        final var restoreKey = new SecretKeySpec(key, "HmacSHA256");
        final var mac = Mac.getInstance(restoreKey.getAlgorithm());
        mac.init(restoreKey);
        return mac.doFinal(message);
    }

    public byte[] sha256Hash(byte[] message) throws NoSuchAlgorithmException {
        final var md = MessageDigest.getInstance("SHA-256");
        md.update(message);
        return md.digest();
    }
}
