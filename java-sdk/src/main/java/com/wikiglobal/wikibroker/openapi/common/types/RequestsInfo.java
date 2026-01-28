package com.wikiglobal.wikibroker.openapi.common.types;

public record RequestsInfo(
        HeadersLike headers,
        String method,
        String url,
        String data
) {
}
