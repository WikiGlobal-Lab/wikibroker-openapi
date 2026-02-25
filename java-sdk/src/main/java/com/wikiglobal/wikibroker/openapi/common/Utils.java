package com.wikiglobal.wikibroker.openapi.common;

import com.wikiglobal.wikibroker.openapi.common.interfaces.RequestReader;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

@UtilityClass
public class Utils {
    public boolean isRequestUsePostMethod(@NonNull RequestReader req) {
        return req.method().equalsIgnoreCase("POST");
    }
}
