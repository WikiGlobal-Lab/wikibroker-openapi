using WikiBroker.OpenApi.Sdk.Adapters;
using WikiBroker.OpenApi.Sdk.Common.Enums;

namespace WikiBroker.OpenApi.Sdk
{
    public static class WikiBrokerOpenApi
    {
        public static void AddXHeaders(
            HttpRequestMessage req,
            Guid apiKey,
            DateTimeOffset timestamp,
            Guid nonce
        )
        {
            req.Headers.Add(CustomHeaders.ApiKey.Value, apiKey.ToString());
            req.Headers.Add(CustomHeaders.TimeStamp.Value, timestamp.ToUnixTimeMilliseconds().ToString());
            req.Headers.Add(CustomHeaders.Nonce.Value, nonce.ToString());
        }

        public static async Task Sign(
            HttpRequestMessage req,
            string key
        )
        {
            var canonicalString = await Core.GenerateCanonicalString(req);
            var signature = Core.GenerateSignature(key, canonicalString);
            req.Headers.Add(CustomHeaders.Signature.Value, signature);
        }

        public static Func<HttpMessageHandler, DelegatingHandler> CreateDelegatingHandlerConstructor(Guid apiKey,
            string apiSecret)
        {
            return (h) =>
                new HttpRequestDelegatingHandler(
                    h,
                    apiKey,
                    apiSecret,
                    AddXHeaders,
                    Sign,
                    () => DateTimeOffset.UtcNow,
                    Guid.NewGuid
                );
        }
    }
}