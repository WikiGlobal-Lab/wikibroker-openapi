package com.wikiglobal.wikibroker.openapi.common;

import com.wikiglobal.wikibroker.openapi.common.types.RequestsInfo;
import org.jspecify.annotations.NonNull;

public class Utils {
    private Utils() {
    }

    public static boolean isRequestUsePostMethod(@NonNull RequestsInfo req) {
        return req.method().equalsIgnoreCase("POST");
    }
}
