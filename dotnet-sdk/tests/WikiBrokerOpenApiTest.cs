using System.Net.Http.Json;
using System.Text.Json;
using WikiBroker.OpenApi.Sdk;
using WikiBroker.OpenApi.Sdk.Adapters;
using WikiBroker.OpenApi.Sdk.Common.Enums;

namespace WikiBroker.OpenApi.Tests
{
    public class WikiBrokerOpenApiTest
    {
        private static readonly JsonSerializerOptions Opt = new()
        {
            PropertyNamingPolicy = JsonNamingPolicy.SnakeCaseLower,
            WriteIndented = false
        };

        private const string BaseUrl = "https://api.example.com";
        private const string Path = "test?q1=c&q2=b&q1=a";

        private static string Url => $"{BaseUrl}/{Path}";

        private static readonly dynamic Body = new { key = "value" };
        private const string Method = "POST";
        private static readonly Guid ApiKey = Guid.Parse("ef05e5b0-9daf-49e3-a0f4-9a3c13f55c3b");
        private const string ApiSecret = "4ae4bf20-0afa-4122-ade8-c0beca7bd5e4";
        private static readonly Guid Nonce = Guid.Parse("4428a206-1afd-4b15-a98d-43e91f49a08d");
        private static readonly DateTimeOffset Timestamp = DateTimeOffset.FromUnixTimeMilliseconds(1798115622000L);

        private const string ExpectedSignature = "1b0c80dbbc30905719559ab5526dfd59bae04d7337c8843efd9e51ff0af6dfb4";

        [Fact]
        public void TestApi()
        {
            var handler = new HttpRequestDelegatingHandler(
                new HttpClientHandler(),
                ApiKey,
                ApiSecret,
                WikiBrokerOpenApi.AddXHeaders,
                WikiBrokerOpenApi.Sign,
                () => Timestamp,
                () => Nonce
            );
            var client = new HttpClient(handler);
            var req = new HttpRequestMessage
            {
                Content = JsonContent.Create(Body, null, Opt),
                Method = new HttpMethod(Method),
                RequestUri = new Uri(Url)
            };
            try
            {
                client.Send(req, new CancellationToken(true));
            }
            catch
            {
            }

            var actualSignature = req.Headers.GetValues(CustomHeaders.Signature.Value).ElementAt(0);
            Assert.Equal(ExpectedSignature, actualSignature);
        }
    }
}