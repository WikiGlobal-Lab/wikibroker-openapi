# WikiBroker OpenAPI SDK for JVM

## 示例

`java.net.http`

```java
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import com.wikiglobal.wikibroker.openapi.WikiBrokerOpenApiNativeRequestBuilderFactory;
// ...
final String API_KEY = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b";
final String API_SECRET = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
final var factory = new WikiBrokerOpenApiNativeRequestBuilderFactory(API_KEY, API_SECRET);

try (var client = HttpClient.newHttpClient()) {
    var builder = factory.create();
    var req = builder.setMethod("POST")
                     .setUrl("https://api.example.com/test?q1=c&q2=b&q1=a")
                     .setBody("{\"key\":\"value\"")
                     .build();
    client.send(req, HttpResponse.BodyHandlers.ofString());
} catch (Exception e) {
    // Handle Exception
}
```

`okhttp`

```java
import okhttp3.OkHttpClient;
import com.wikiglobal.wikibroker.openapi.WikiBrokerOpenApiOkhttpRequestBuilderFactory;
// ...
final String API_KEY = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b";
final String API_SECRET = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
final var factory = new WikiBrokerOpenApiOkhttpRequestBuilderFactory(API_KEY, API_SECRET);

var client = new OkHttpClient();
try {
    var builder = factory.create();
    var req = builder.setMethod("POST")
                     .setUrl("https://api.example.com/test?q1=c&q2=b&q1=a")
                     .setBody("{\"key\":\"value\"")
                     .build();
    try (var resp = client.newCall(req).execute()) {
        // Handle Response
    }
} catch (Exception e) {
    // Handle Exception
}
```

`apache httpclient`

```java
import org.apache.hc.client5.http.impl.classic.HttpClients;
import com.wikiglobal.wikibroker.openapi.WikiBrokerOpenApiApacheRequestBuilderFactory;
// ...
final String API_KEY = "ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b";
final String API_SECRET = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
final var factory = new WikiBrokerOpenApiApacheRequestBuilderFactory(API_KEY, API_SECRET);

try (var client = HttpClients.createDefault()) {
    var builder = factory.create();
    var req = builder.setMethod("POST")
                     .setUrl("https://api.example.com/test?q1=c&q2=b&q1=a")
                     .setBody("{\"key\":\"value\"")
                     .build();
    client.execute(
        req, resp -> {
            // Handle Response
            return null;
        }
    );
} catch (Exception e) {
    // Handle Exception
}
```
