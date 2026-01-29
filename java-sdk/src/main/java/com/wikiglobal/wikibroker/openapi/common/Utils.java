package com.wikiglobal.wikibroker.openapi.common;

import com.wikiglobal.wikibroker.openapi.common.types.RequestReader;
import org.jspecify.annotations.NonNull;

public class Utils {
    private Utils() {
    }

    public static boolean isRequestUsePostMethod(@NonNull RequestReader req) {
        return req.getMethod().equalsIgnoreCase("POST");
    }
}
