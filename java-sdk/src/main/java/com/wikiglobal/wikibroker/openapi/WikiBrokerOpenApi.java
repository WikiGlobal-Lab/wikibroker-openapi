package com.wikiglobal.wikibroker.openapi;

import com.wikiglobal.wikibroker.openapi.common.enums.CustomHeaders;
import com.wikiglobal.wikibroker.openapi.common.interfaces.*;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

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

    public class RequestBuilderFactory<T> implements Factory<RequestBuilder<T>> {
        private final UUID apiKey;
        private final String apiSecret;

        public RequestBuilderFactory(String apiKey, String apiSecret, Factory.Type type) {
            this.apiKey = UUID.fromString(apiKey);
            this.apiSecret = apiSecret;
            this.init(type);
        }

        private Supplier<RequestBuilder<T>> create;

        @SneakyThrows
        private void init(Factory.@NonNull Type t) {
            final var builderClass = Class.forName(t.value());
            final var builderBuilder = builderClass.getDeclaredMethod("builder").invoke(null);
            final var builderBuilderClass = builderBuilder.getClass();
            final var apiKeyMethod = builderBuilderClass.getMethod("apiKey", UUID.class);
            final var apiSecretMethod = builderBuilderClass.getMethod("apiSecret", String.class);
            final var loadHeadersMethod = builderBuilderClass.getMethod(
                "loadHeaders",
                LoadHeaders.class
            );
            final var signMethod = builderBuilderClass.getMethod("sign", Sign.class);
            final var timestampGeneratorMethod = builderBuilderClass.getMethod(
                "timestampGenerator",
                Supplier.class
            );
            final var idGeneratorMethod = builderBuilderClass.getMethod(
                "idGenerator",
                Supplier.class
            );
            final var buildBuilderMethod = builderBuilderClass.getDeclaredMethod("build");
            buildBuilderMethod.setAccessible(true);
            this.create = () -> {
                try {
                    apiKeyMethod.invoke(builderBuilder, this.apiKey);
                    apiSecretMethod.invoke(builderBuilder, this.apiSecret);
                    loadHeadersMethod.invoke(
                        builderBuilder,
                        (LoadHeaders<?>) WikiBrokerOpenApi::addXHeaders
                    );
                    signMethod.invoke(builderBuilder, (Sign<?>) WikiBrokerOpenApi::sign);
                    timestampGeneratorMethod.invoke(
                        builderBuilder,
                        (Supplier<Instant>) Instant::now
                    );
                    idGeneratorMethod.invoke(builderBuilder, (Supplier<UUID>) UUID::randomUUID);
                    @SuppressWarnings("unchecked") final var builder = (
                        (RequestBuilder<T>) buildBuilderMethod.invoke(builderBuilder)
                    );
                    return builder;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }

        @Override
        public RequestBuilder<T> create() {
            return this.create.get();
        }
    }
}
