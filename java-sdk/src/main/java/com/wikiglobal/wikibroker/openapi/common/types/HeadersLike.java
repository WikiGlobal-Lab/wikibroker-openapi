package com.wikiglobal.wikibroker.openapi.common.types;

import org.jspecify.annotations.NonNull;

public interface HeadersLike {
    void set(@NonNull String field, @NonNull String value);

    String get(@NonNull String field);
}
